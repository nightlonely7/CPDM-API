package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.configurations.AuthenticationFacade;
import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.TaskFilesEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.BadRequestException;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.forms.tasks.TaskCreateForm;
import com.fpt.cpdm.forms.tasks.TaskSearchForm;
import com.fpt.cpdm.forms.tasks.TaskUpdateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskBasic;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.*;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskFilesRepository taskFilesRepository;
    private final AuthenticationFacade authenticationFacade;
    private final AssignRequestRepository assignRequestRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, DocumentRepository documentRepository, DepartmentRepository departmentRepository, TaskFilesRepository taskFilesRepository, AuthenticationFacade authenticationFacade, AssignRequestRepository assignRequestRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.documentRepository = documentRepository;
        this.departmentRepository = departmentRepository;
        this.taskFilesRepository = taskFilesRepository;
        this.authenticationFacade = authenticationFacade;
        this.assignRequestRepository = assignRequestRepository;
    }

    @Override
    public void uploadFile(Integer id, String filename) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );
        TaskFilesEntity taskFilesEntity = new TaskFilesEntity();
        taskFilesEntity.setTask(taskEntity);
        taskFilesEntity.setFilename(filename);
        taskFilesRepository.save(taskFilesEntity);
    }

    @Override
    public TaskDetail findDetailById(Integer id) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        if (taskRepository.existsByCreatorOrExecutorOrRelatives(userEntity, userEntity, userEntity) == false) {
            throw new UnauthorizedException();
        }

        TaskDetail taskDetail = taskRepository.findDetailById(id);

        return taskDetail;
    }


    @Override
    public TaskSummary changeStatus(Task task) {

        TaskEntity taskEntity = taskRepository.findById(task.getId()).orElseThrow(
                () -> new TaskNotFoundException(task.getId())
        );

        // check if executor related to task
        if (task.getExecutor().getId().equals(taskEntity.getExecutor().getId()) == false) {
            throw new UnauthorizedException();
        }

        taskEntity.setStatus(task.getStatus());
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        TaskSummary savedTaskSummary = taskRepository.findSummaryById(savedTaskEntity.getId());

        return savedTaskSummary;
    }

//    @Override
//    public TaskSummary save(Task task) {
//
//        // check task exist (can be null)
//        if (task.getId() != null && taskRepository.existsById(task.getId()) == false) {
//            throw new TaskNotFoundException(task.getId());
//        }
//
//        // check end time after start time
//        if (task.getEndTime().isBefore(task.getStartTime())) {
//            throw new TaskTimeException("End time is before start time!");
//        }
//
//        // find executor
//        UserEntity executor = userRepository.findById(task.getExecutor().getId()).orElseThrow(
//                () -> new UserNotFoundException(task.getExecutor().getId())
//        );
//
//        // find creator
//        UserEntity creator = userRepository.findById(task.getCreator().getId()).orElseThrow(
//                () -> new UserNotFoundException(task.getCreator().getId())
//        );
//
//        // check executor and creator in the same department
//        if (executor.getDepartment().equals(creator.getDepartment()) == false) {
//            throw new UnauthorizedException();
//        }
//
//        // check parent task exists (can be null)
//        if (task.getParentTask() != null && taskRepository.existsById(task.getParentTask().getId()) == false) {
//            throw new TaskNotFoundException(task.getParentTask().getId());
//        }
//
//        // check documents exist (can be null)
//        if (task.getDocuments() != null && task.getDocuments().isEmpty() == false) {
//            for (Document document : task.getDocuments()) {
//                if (documentRepository.existsById(document.getId()) == false) {
//                    throw new DocumentNotFoundException(document.getId());
//                }
//            }
//        }
//
//        TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);
//        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
//        TaskSummary savedTaskSummary = taskRepository.findSummaryById(savedTaskEntity.getId());
//
//        return savedTaskSummary;
//    }

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
            parentTask = new TaskEntity();
            parentTask.setId(taskCreateForm.getParentTask().getId());
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

        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        TaskDetail taskDetail = taskRepository.findDetailById(savedTaskEntity.getId());

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
        TaskDetail taskDetail = taskRepository.findDetailById(savedTaskEntity.getId());

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
                taskSearchForm.getTitle(), taskSearchForm.getSummary(),
                taskSearchForm.getCreatedTimeFrom(), taskSearchForm.getCreatedTimeTo(),
                taskSearchForm.getStartTimeFrom(), taskSearchForm.getStartTimeTo(),
                taskSearchForm.getEndTimeFrom(), taskSearchForm.getEndTimeTo(),
                taskSearchForm.getProjectId(), pageable);

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
                taskSearchForm.getTitle(), taskSearchForm.getSummary(),
                taskSearchForm.getCreatedTimeFrom(), taskSearchForm.getCreatedTimeTo(),
                taskSearchForm.getStartTimeFrom(), taskSearchForm.getStartTimeTo(),
                taskSearchForm.getEndTimeFrom(), taskSearchForm.getEndTimeTo(),
                taskSearchForm.getProjectId(), pageable);

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
                taskSearchForm.getTitle(), taskSearchForm.getSummary(),
                taskSearchForm.getStartTimeFrom(), taskSearchForm.getStartTimeTo(),
                taskSearchForm.getCreatedTimeFrom(), taskSearchForm.getCreatedTimeTo(),
                taskSearchForm.getEndTimeFrom(), taskSearchForm.getEndTimeTo(),
                taskSearchForm.getProjectId(), pageable);

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
    public List<TaskBasic> findAllBasicByCurrentExecutorAndProject_Id(Integer projectId) {
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );
        List<TaskBasic> taskBasics = taskRepository.findAllBasicByExecutorAndProject_Id(executor, projectId);

        return taskBasics;
    }

    @Override
    public boolean existsByExecutorAndStatusAndStartTimeIsBetween(User user, String status, LocalDateTime fromTime, LocalDateTime toTime) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return taskRepository.existsByExecutorAndStatusAndStartTimeIsBetween(userEntity, status, fromTime, toTime);
    }

    @Override
    public boolean existsByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(User user, String status, LocalDateTime fromTime) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return taskRepository.existsByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(userEntity, status, fromTime, fromTime);
    }

    @Override
    public List<TaskSummary> findAllByExecutorAndStatusAndStartTimeIsBetween(User user, String status, LocalDateTime fromTime, LocalDateTime toTime) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return taskRepository.findAllByExecutorAndStatusAndStartTimeIsBetween(userEntity, status, fromTime, toTime);
    }

    @Override
    public List<TaskSummary> findAllByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(User user, String status, LocalDateTime fromTime) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return taskRepository.findAllByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(userEntity, status, fromTime, fromTime);
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndNotAssigned(String status, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<TaskSummary> taskSummaries = taskRepository.findAllByExecutorAndStatusAndStartTimeIsBetween(executor,status,fromTime,toTime);
        taskSummaries.addAll(taskRepository.findAllByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(executor,status,fromTime,fromTime));

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        for (TaskSummary taskSummary : taskSummaries) {
            //check exist assign request status new or approved
            if(assignRequestRepository.existsByUserAndStatusInAndFromDateIsBetween(executor,integerList,taskSummary.getStartTime().toLocalDate(),taskSummary.getEndTime().toLocalDate())){
                taskSummaries.remove(taskSummary);
            }
            if(assignRequestRepository.existsByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(executor,integerList,taskSummary.getStartTime().toLocalDate(),taskSummary.getStartTime().toLocalDate())){
                taskSummaries.remove(taskSummary);
            }
        }

        Page<TaskSummary> result = new PageImpl<TaskSummary>(taskSummaries,new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()),taskSummaries.size());
        return result;
    }

    @Override
    public Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndAssigned(String status, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable) {
        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity executor = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<TaskSummary> taskSummaries = taskRepository.findAllByExecutorAndStatusAndStartTimeIsBetween(executor,status,fromTime,toTime);
        taskSummaries.addAll(taskRepository.findAllByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(executor,status,fromTime,fromTime));

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        List<TaskSummary> taskSummariesAssigned = new ArrayList<>();
        for (TaskSummary taskSummary : taskSummaries) {
            //check exist assign request status new or approved
            if(assignRequestRepository.existsByUserAndStatusInAndFromDateIsBetween(executor,integerList,taskSummary.getStartTime().toLocalDate(),taskSummary.getEndTime().toLocalDate())){
                taskSummariesAssigned.add(taskSummary);
            }
            if(assignRequestRepository.existsByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(executor,integerList,taskSummary.getStartTime().toLocalDate(),taskSummary.getStartTime().toLocalDate())){
                taskSummariesAssigned.add(taskSummary);
            }
        }

        Page<TaskSummary> result = new PageImpl<TaskSummary>(taskSummariesAssigned,new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()),taskSummariesAssigned.size());
        return result;
    }
}
