package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.configurations.AuthenticationFacade;
import com.fpt.cpdm.entities.*;
import com.fpt.cpdm.exceptions.BadRequestException;
import com.fpt.cpdm.exceptions.ConflictException;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.forms.tasks.TaskCreateForm;
import com.fpt.cpdm.forms.tasks.TaskSearchForm;
import com.fpt.cpdm.forms.tasks.TaskUpdateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueStatus;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.repositories.*;
import com.fpt.cpdm.services.TaskHistoryService;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.utils.ConstantManager;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelConverter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskIssueRepository taskIssueRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskFilesRepository taskFilesRepository;
    private final AuthenticationFacade authenticationFacade;
    private final AssignRequestRepository assignRequestRepository;
    private final TaskHistoryService taskHistoryService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, TaskIssueRepository taskIssueRepository, UserRepository userRepository, DocumentRepository documentRepository, DepartmentRepository departmentRepository, TaskFilesRepository taskFilesRepository, AuthenticationFacade authenticationFacade, AssignRequestRepository assignRequestRepository, TaskHistoryService taskHistoryService) {
        this.taskRepository = taskRepository;
        this.taskIssueRepository = taskIssueRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.departmentRepository = departmentRepository;
        this.taskFilesRepository = taskFilesRepository;
        this.authenticationFacade = authenticationFacade;
        this.assignRequestRepository = assignRequestRepository;
        this.taskHistoryService = taskHistoryService;
    }

    @Override
    public void uploadFile(Integer id, String filename) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );
        TaskFileEntity taskFileEntity = new TaskFileEntity();
        taskFileEntity.setTask(taskEntity);
        taskFileEntity.setFilename(filename);
        taskFilesRepository.save(taskFileEntity);
    }

    @Override
    public TaskDetail findDetailById(Integer id) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity current = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        if (current.getRole().getName().equals("ROLE_ADMIN") == false
                && taskRepository.existsByCreatorAndIdOrExecutorAndIdOrRelativesAndId(
                current, id, current, id, current, id) == false) {
            throw new UnauthorizedException();
        }

        TaskDetail taskDetail = taskRepository.findDetailByIdAndAvailableTrue(id);

        return taskDetail;
    }

    @Override
    public TaskIssueStatus findIssueStatusById(Integer id) {

        Integer total = taskIssueRepository.countAllByTask_IdAndAvailableTrue(id);
        Integer completed = taskIssueRepository.countAllByTask_IdAndCompletedTrueAndAvailableTrue(id);
        TaskIssueStatus taskIssueStatus = new TaskIssueStatus(total, completed);

        return taskIssueStatus;
    }


    @Override
    public TaskSummary complete(Integer id) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity current = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );

        // check if executor related to task
        if (taskEntity.getExecutor().equals(current) == false) {
            throw new UnauthorizedException();
        }

        // check if task is not currently running
        if (taskEntity.getStatus().equals("Working") == false
                && taskEntity.getStatus().equals("Outdated") == false
                && taskEntity.getStatus().equals("Near deadline") == false) {
            throw new ConflictException("This task is not currently running");
        }

        // check all issues is completed
        if (taskIssueRepository.existsByTask_IdAndCompletedFalseAndAvailableTrue(id)) {
            throw new ConflictException("All issues must be completed first to complete this task!");
        }

        if (taskEntity.getEndTime().isBefore(LocalDateTime.now())) {
            taskEntity.setStatus("Complete outdated");
        } else {
            taskEntity.setStatus("Completed");
        }
        taskEntity.setCompletedTime(LocalDateTime.now());
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        TaskSummary savedTaskSummary = taskRepository.findSummaryById(savedTaskEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedTaskEntity.getId(), "Task")
        );

        return savedTaskSummary;
    }

    @Override
    public TaskDetail create(TaskCreateForm taskCreateForm) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity creator = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        UserEntity executor = new UserEntity();
        executor.setId(taskCreateForm.getExecutor().getId());

        ProjectEntity project = new ProjectEntity();
        project.setId(taskCreateForm.getProject().getId());

        TaskEntity parentTask = null;
        if (taskCreateForm.getParentTask() != null) {
            parentTask = taskRepository.findById(taskCreateForm.getParentTask().getId()).orElseThrow(
                    () -> new EntityNotFoundException(taskCreateForm.getParentTask().getId(), "Task")
            );
        }


        List<UserEntity> relatives = new ArrayList<>();
        if (taskCreateForm.getRelatives() != null) {
            for (IdOnlyForm idOnlyForm : taskCreateForm.getRelatives()) {
                UserEntity relative = new UserEntity(idOnlyForm.getId());
                relatives.add(relative);
            }
        }



        TaskEntity taskEntity = TaskEntity.builder()
                .project(project)
                .creator(creator)
                .executor(executor)
                .relatives(relatives)
                .parentTask(parentTask)
                .priority(taskCreateForm.getPriority())
                .title(taskCreateForm.getTitle())
                .summary(taskCreateForm.getSummary())
                .description(taskCreateForm.getDescription())
                .startTime(taskCreateForm.getStartTime())
                .endTime(taskCreateForm.getEndTime())
                .build();

        if (taskCreateForm.getStartTime() == null) {
            taskEntity.setStartTime(LocalDateTime.now());
        }

        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        taskHistoryService.save(savedTaskEntity);

        TaskDetail taskDetail = taskRepository.findDetailByIdAndAvailableTrue(savedTaskEntity.getId());

        return taskDetail;
    }

    @Override
    public TaskDetail update(Integer id, TaskUpdateForm taskUpdateForm) {

        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );

        UserEntity executor = new UserEntity();
        executor.setId(taskUpdateForm.getExecutor().getId());

        ProjectEntity project = new ProjectEntity();
        project.setId(taskUpdateForm.getProject().getId());

        TaskEntity parentTask = null;
        if (taskUpdateForm.getParentTask() != null) {
            parentTask = new TaskEntity();
            parentTask.setId(taskUpdateForm.getParentTask().getId());
        }

        taskEntity.setProject(project);
        taskEntity.setExecutor(executor);
        taskEntity.setParentTask(parentTask);
        taskEntity.setTitle(taskUpdateForm.getTitle());
        taskEntity.setSummary(taskUpdateForm.getSummary());
        taskEntity.setDescription(taskUpdateForm.getDescription());
        taskEntity.setStartTime(taskUpdateForm.getStartTime());
        taskEntity.setEndTime(taskUpdateForm.getEndTime());
        taskEntity.setPriority(taskUpdateForm.getPriority());

        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        taskHistoryService.save(savedTaskEntity);

        TaskDetail taskDetail = taskRepository.findDetailByIdAndAvailableTrue(savedTaskEntity.getId());

        return taskDetail;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutor(TaskSearchForm taskSearchForm, Pageable pageable) {

        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );


        if (taskSearchForm.getCreatedTimeFrom() != null && taskSearchForm.getCreatedTimeTo() != null
                && taskSearchForm.getCreatedTimeFrom().isAfter(taskSearchForm.getCreatedTimeTo())) {
            throw new BadRequestException("createdTimeFrom is after createdTimeTo");
        }

        if (taskSearchForm.getStartTimeFrom() != null && taskSearchForm.getStartTimeTo() != null
                && taskSearchForm.getStartTimeFrom().isAfter(taskSearchForm.getStartTimeTo())) {
            throw new BadRequestException("startTimeFrom is after startTimeTo");
        }

        if (taskSearchForm.getEndTimeFrom() != null && taskSearchForm.getEndTimeTo() != null
                && taskSearchForm.getEndTimeFrom().isAfter(taskSearchForm.getEndTimeTo())) {
            throw new BadRequestException("endTimeFrom is after endTimeTo");
        }

        return taskRepository.advanceSearch(null, executor, null,
                taskSearchForm.getTitle(), taskSearchForm.getSummary(), taskSearchForm.getDescription(),
                taskSearchForm.getCreatedTimeFrom(), taskSearchForm.getCreatedTimeTo(),
                taskSearchForm.getStartTimeFrom(), taskSearchForm.getStartTimeTo(),
                taskSearchForm.getEndTimeFrom(), taskSearchForm.getEndTimeTo(),
                taskSearchForm.getProjectId(), taskSearchForm.getStatus(), pageable);

    }

    @Override
    public List<TaskSummary> findAllSummaryByExecutor() {
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );
        return taskRepository.findAllSummaryByExecutorAndAvailableTrue(executor);
    }

    @Override
    public Page<TaskSummary> findAllSummaryByCreator(TaskSearchForm taskSearchForm, Pageable pageable) {

        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity creator = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );


        if (taskSearchForm.getCreatedTimeFrom() != null && taskSearchForm.getCreatedTimeTo() != null
                && taskSearchForm.getCreatedTimeFrom().isAfter(taskSearchForm.getCreatedTimeTo())) {
            throw new BadRequestException("createdTimeFrom is after createdTimeTo");
        }

        if (taskSearchForm.getStartTimeFrom() != null && taskSearchForm.getStartTimeTo() != null
                && taskSearchForm.getStartTimeFrom().isAfter(taskSearchForm.getStartTimeTo())) {
            throw new BadRequestException("startTimeFrom is after startTimeTo");
        }

        if (taskSearchForm.getEndTimeFrom() != null && taskSearchForm.getEndTimeTo() != null
                && taskSearchForm.getEndTimeFrom().isAfter(taskSearchForm.getEndTimeTo())) {
            throw new BadRequestException("endTimeFrom is after endTimeTo");
        }

        return taskRepository.advanceSearch(creator, null, null,
                taskSearchForm.getTitle(), taskSearchForm.getSummary(), taskSearchForm.getDescription(),
                taskSearchForm.getCreatedTimeFrom(), taskSearchForm.getCreatedTimeTo(),
                taskSearchForm.getStartTimeFrom(), taskSearchForm.getStartTimeTo(),
                taskSearchForm.getEndTimeFrom(), taskSearchForm.getEndTimeTo(),
                taskSearchForm.getProjectId(), taskSearchForm.getStatus(), pageable);

    }

    @Override
    public List<TaskSummary> findAllSummaryByCreator() {
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity creator = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );
        return taskRepository.findAllSummaryByCreatorAndAvailableTrue(creator);
    }

    @Override
    public Page<TaskSummary> findAllSummaryByRelatives(TaskSearchForm taskSearchForm, Pageable pageable) {

        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity relative = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );


        if (taskSearchForm.getCreatedTimeFrom() != null && taskSearchForm.getCreatedTimeTo() != null
                && taskSearchForm.getCreatedTimeFrom().isAfter(taskSearchForm.getCreatedTimeTo())) {
            throw new BadRequestException("createdTimeFrom is after createdTimeTo");
        }

        if (taskSearchForm.getStartTimeFrom() != null && taskSearchForm.getStartTimeTo() != null
                && taskSearchForm.getStartTimeFrom().isAfter(taskSearchForm.getStartTimeTo())) {
            throw new BadRequestException("startTimeFrom is after startTimeTo");
        }

        if (taskSearchForm.getEndTimeFrom() != null && taskSearchForm.getEndTimeTo() != null
                && taskSearchForm.getEndTimeFrom().isAfter(taskSearchForm.getEndTimeTo())) {
            throw new BadRequestException("endTimeFrom is after endTimeTo");
        }

        return taskRepository.advanceSearch(null, null, relative,
                taskSearchForm.getTitle(), taskSearchForm.getSummary(), taskSearchForm.getDescription(),
                taskSearchForm.getStartTimeFrom(), taskSearchForm.getStartTimeTo(),
                taskSearchForm.getCreatedTimeFrom(), taskSearchForm.getCreatedTimeTo(),
                taskSearchForm.getEndTimeFrom(), taskSearchForm.getEndTimeTo(),
                taskSearchForm.getProjectId(), taskSearchForm.getStatus(), pageable);

    }

    @Override
    public List<TaskSummary> findAllSummaryByRelatives() {
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity relative = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        return taskRepository.findAllSummaryByRelativesAndAvailableTrue(relative);
    }

    @Scheduled(fixedRate = 30000)
    public void manageStatus() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("manage task status " + System.currentTimeMillis());
        List<TaskEntity> taskEntities = taskRepository.findAll();
        for (TaskEntity taskEntity : taskEntities) {
            boolean changed = false;
            if (taskEntity.getStatus().equals("Waiting")
                    && now.isAfter(taskEntity.getStartTime())) {
                taskEntity.setStatus("Working");
                changed = true;
            }
            if (!changed
                    && taskEntity.getStatus().equals("Working")
                    && now.isAfter(taskEntity.getEndTime())) {
                taskEntity.setStatus("Outdated");
                changed = true;
            }
            if (!changed
                    && taskEntity.getStatus().equals("Working")
                    && now.isBefore(taskEntity.getEndTime())) {
                long duration = ChronoUnit.MINUTES.between(taskEntity.getStartTime(), taskEntity.getEndTime());
                LocalDateTime warningSpot = taskEntity.getEndTime().minus(duration / 5, ChronoUnit.MINUTES);
                if (now.isAfter(warningSpot)) {
                    taskEntity.setStatus("Near deadline");
                    changed = true;
                }
            }
            if (changed) {
                System.out.println("changed");
                taskRepository.save(taskEntity);
            }
        }
        System.out.println("manage task status done " + System.currentTimeMillis());
    }

    @Scheduled(fixedRate = 30000)
    public void switchExecutorAuto(){
        LocalDate today = LocalDate.now();
        System.out.println("switch executor auto " + System.currentTimeMillis());

        Integer approvedCode = Enum.AssignRequestStatus.Approved.getAssignRequestStatusCode();
        //find all assign request approved, from date less than equal today but not change executor
        List<AssignRequestEntity> notStartedApprovedAssignRequests = assignRequestRepository.findAllByStatusAndFromDateLessThanEqualAndStartedFalseAndFinishedFalse(approvedCode,today);
        for (AssignRequestEntity assignRequestEntity: notStartedApprovedAssignRequests) {

            UserEntity user = assignRequestEntity.getUser();
            UserEntity assignee = assignRequestEntity.getAssignee();
            UserEntity approver = assignRequestEntity.getApprover();
            List<TaskEntity> taskEntities = assignRequestEntity.getTasks();
            for (TaskEntity taskEntity : taskEntities) {
                //Add current executor to relatives
                taskEntity.getRelatives().add(user);
                //Change executor to assignee
                taskEntity.setExecutor(assignee);
                //save history
                TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
                taskHistoryService.save(savedTaskEntity, approver);
            }
            //update started status to true
            assignRequestEntity.setStarted(true);
            assignRequestRepository.save(assignRequestEntity);
        }
        //find all assign request approved, to date less than equal today but not change executor back
        List<AssignRequestEntity> notFinshedApprovedAssignRequests = assignRequestRepository.findAllByStatusAndToDateLessThanEqualAndStartedTrueAndFinishedFalse(approvedCode,today);
        for (AssignRequestEntity assignRequestEntity: notFinshedApprovedAssignRequests) {
            UserEntity user = assignRequestEntity.getUser();
            UserEntity approver = assignRequestEntity.getApprover();
            for (TaskEntity taskEntity : assignRequestEntity.getTasks()) {
                //Remove old executor from relatives
                taskEntity.getRelatives().remove(user);
                //Change executor back to old executor
                taskEntity.setExecutor(user);
                //save history
                TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
                taskHistoryService.save(savedTaskEntity,approver);
            }
            //update finised status to true
            assignRequestEntity.setFinished(true);
            assignRequestRepository.save(assignRequestEntity);
        }

        System.out.println("switch executor done " + System.currentTimeMillis());
    }

    @Override
    public void deleteById(Integer id) {

        TaskEntity taskEntity = taskRepository.findById(id).get();
        taskEntity.setAvailable(false);
        taskRepository.save(taskEntity);
    }

    @Override
    public Page<TaskSummary> findAllByParentTask_Id(Integer taskId, Pageable pageable) {
        Page<TaskSummary> taskSummaries = taskRepository.findAllByParentTask_Id(taskId, pageable);

        return taskSummaries;
    }

    @Override
    public List<TaskSummary> findAllBasicByCurrentExecutorAndProject_Id(Integer projectId) {
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );
        List<TaskSummary> taskSummaries = taskRepository.findAllBasicByExecutorAndProject_Id(executor, projectId);

        return taskSummaries;
    }

    @Override
    public boolean existsByExecutorAndStatusAndStartTimeLessThanEqualAndStartTimeGreaterThanEqual(User user, String status, LocalDateTime fromTime, LocalDateTime toTime) {
        UserEntity executor = ModelConverter.userModelToEntity(user);
        return taskRepository.existsByExecutorAndStatusAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(executor, status, fromTime, toTime);
    }

    @Override
    public boolean existsByExecutorAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(User user, String status, LocalDateTime fromTime) {
        UserEntity executor = ModelConverter.userModelToEntity(user);
        return taskRepository.existsByExecutorAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(executor, status, fromTime, fromTime);
    }

    @Override
    public List<TaskSummary> findAllByExecutorAndStatusInAndStartTimeLessThanEqualAndStartTimeGreaterThanEqual(User user, List<String> listStatus, LocalDateTime fromTime, LocalDateTime toTime) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return taskRepository.findAllByExecutorAndStatusInAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(userEntity, listStatus, fromTime, toTime);
    }

    @Override
    public List<TaskSummary> findAllByExecutorAndStatusInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(User user, List<String> listStatus, LocalDateTime fromTime) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return taskRepository.findAllByExecutorAndStatusInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(userEntity, listStatus, fromTime, fromTime);
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndNotAssigned(LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<TaskSummary> taskSummaries = this.findAllSummaryByExecutorAndDateRangeAndNotComplete(fromTime, toTime);

        LocalDate fromDate = fromTime.toLocalDate();
        LocalDate toDate = toTime.toLocalDate();

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        List<AssignRequestSummary> assignRequestSummaries = assignRequestRepository.findAllByUserAndStatusInAndFromDateAfterAndFromDateLessThanEqual(executor, integerList, fromDate, toDate);
        assignRequestSummaries.addAll(assignRequestRepository.findAllByUserAndStatusInAndToDateGreaterThanEqualAndToDateBefore(executor, integerList, fromDate, toDate));
        assignRequestSummaries.addAll(assignRequestRepository.findAllByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(executor, integerList, fromDate, toDate));
        List<Integer> assignedTaskSummaryIds = new ArrayList<>();
        for (AssignRequestSummary assignRequestSummary : assignRequestSummaries) {
            assignRequestSummary.getTasks().forEach(o -> assignedTaskSummaryIds.add(o.getId()));
        }
        taskSummaries.removeIf(taskSummary -> assignedTaskSummaryIds.contains(taskSummary.getId()));

        Page<TaskSummary> result = new PageImpl<TaskSummary>(taskSummaries, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), taskSummaries.size());
        return result;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndFullAssigned(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        List<TaskSummary> taskSummaries = new ArrayList<>();

        //check exist assign request status new or approved which fromDate <= startTime and toDate >= EndTime
        List<AssignRequestSummary> assignRequestSummaries = assignRequestRepository.findAllByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(executor, integerList, fromDate, toDate);
        for (AssignRequestSummary assignRequestSummary : assignRequestSummaries) {
            taskSummaries.addAll(assignRequestSummary.getTasks());
        }

        Page<TaskSummary> result = new PageImpl<TaskSummary>(taskSummaries, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), taskSummaries.size());
        return result;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndPartAssigned(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        List<TaskSummary> taskSummaries = new ArrayList<>();

        //check exist assign request status new or approved which have only some part lane on date range
        List<AssignRequestSummary> assignRequestSummaries = assignRequestRepository.findAllByUserAndStatusInAndFromDateAfterAndFromDateLessThanEqual(executor, integerList, fromDate, toDate);
        assignRequestSummaries.addAll(assignRequestRepository.findAllByUserAndStatusInAndToDateGreaterThanEqualAndToDateBefore(executor, integerList, fromDate, toDate));
        for (AssignRequestSummary assignRequestSummary : assignRequestSummaries) {
            for (TaskSummary taskSummary : assignRequestSummary.getTasks()) {
                if (!taskSummaries.contains(taskSummary)) taskSummaries.add(taskSummary);
            }
        }

        Page<TaskSummary> result = new PageImpl<TaskSummary>(taskSummaries, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), taskSummaries.size());
        return result;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndNotComplete(LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable) {
        List<TaskSummary> taskSummaries = this.findAllSummaryByExecutorAndDateRangeAndNotComplete(fromTime,toTime);
        Page<TaskSummary> result = new PageImpl<TaskSummary>(taskSummaries, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), taskSummaries.size());
        return result;
    }

    @Override
    public List<TaskSummary> findAllSummaryByExecutorAndDateRangeAndNotComplete(LocalDateTime fromTime, LocalDateTime toTime) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<String> listStatus = ConstantManager.NOT_COMPLETE_STATUS_LIST;
        List<TaskSummary> taskSummaries = taskRepository.findAllByExecutorAndStatusInAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(executor, listStatus, fromTime, toTime);
        taskSummaries.addAll(taskRepository.findAllByExecutorAndStatusInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(executor, listStatus, fromTime, fromTime));
        //add all outdated task
        taskSummaries.addAll(taskRepository.findAllByExecutorAndStatusAndAvailableTrue(executor, "Outdated"));

        return taskSummaries;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndStatus(String status, Pageable pageable) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        Page<TaskSummary> result = taskRepository.findAllByExecutorAndStatusAndAvailableTrue(executor,status,pageable);
        return result;
    }

    @Override
    public boolean existsByExecutorAndStatus(String status) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        return taskRepository.existsByExecutorAndStatus(executor,status);
    }
}
