package com.fpt.cpdm.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fpt.cpdm.entities.LeaveRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.leaveRequests.LeaveRequestNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.PolicyForLeave;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.AssignRequestRepository;
import com.fpt.cpdm.repositories.LeaveRequestRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.LeaveRequestService;
import com.fpt.cpdm.utils.ConstantManager;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private LeaveRequestRepository leaveRequestRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private AssignRequestRepository assignRequestRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository, TaskRepository taskRepository, AssignRequestRepository assignRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.assignRequestRepository = assignRequestRepository;
    }

    @Override
    public Page<LeaveRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestRepository.findAllSummaryByUserAndStatus(userEntity, status, pageable);
        return leaveRequestSummaries;
    }

    @Override
    public List<LeaveRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        List<LeaveRequestSummary> leaveRequestSummaries = leaveRequestRepository.findAllSummaryByUserAndStatus(userEntity, status);
        return leaveRequestSummaries;
    }

    @Override
    public Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User approver, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(approver);
        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestRepository.findAllSummaryByApproverAndStatus(userEntity, status, pageable);
        return leaveRequestSummaries;
    }

    @Override
    public List<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User approver, Integer status) {
        UserEntity userEntity = ModelConverter.userModelToEntity(approver);
        List<LeaveRequestSummary> leaveRequestSummaries = leaveRequestRepository.findAllSummaryByApproverAndStatus(userEntity, status);
        return leaveRequestSummaries;
    }

    @Override
    public boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromDate, LocalDate toDate, User user, Integer status) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return leaveRequestRepository.existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(fromDate,toDate,userEntity,status);
    }

    @Override
    public List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateGreaterThanEqualAndFromDateLessThanEqual(User user, List<Integer> integerList, LocalDate fromDate, LocalDate toDate) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return leaveRequestRepository.findAllSummaryByUserAndStatusInAndFromDateGreaterThanEqualAndFromDateLessThanEqual(userEntity, integerList, fromDate, toDate);
    }

    @Override
    public List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(User user, List<Integer> integerList, LocalDate fromDate) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return leaveRequestRepository.findAllSummaryByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(userEntity, integerList, fromDate, fromDate);
    }

    @Override
    public boolean validateNewLeaveRequest(User user, LeaveRequest leaveRequest) {
        //validate to date >= from date
        if(leaveRequest.getToDate().isBefore(leaveRequest.getFromDate())){
            return false;
        }

        //Get number of day off requested
        int diff = (int) DAYS.between(leaveRequest.getFromDate(), leaveRequest.getToDate()) + 1;

        //Get number of day off free check in json file default 3
        Integer numberOfDateFreeCheck = ConstantManager.defaultNumberOfDayOffFreeCheck;
        try {
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            ObjectMapper mapper = new ObjectMapper().registerModule( new JavaTimeModule());
            List<PolicyForLeave> policyForFreeList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class,PolicyForLeave.class));
            if (policyForFreeList.size() > 0) {
                policyForFreeList.sort((o1, o2) -> o1.getValidFromDate().compareTo(o2.getValidFromDate()));
                LocalDate fromDate = leaveRequest.getFromDate();
                for (PolicyForLeave item : policyForFreeList) {
                    if (fromDate.isAfter(item.getValidFromDate())) {
                        if (item.getNumberOfDayOffFreeCheck() != null) {
                            numberOfDateFreeCheck = item.getNumberOfDayOffFreeCheck();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check number of day of policy rule
        //Deny if exist working task if request days greater than policy free check days
        if (diff > numberOfDateFreeCheck) {
            UserEntity userEntity = ModelConverter.userModelToEntity(user);
            List<Integer> integerList = new ArrayList<>();
            integerList.add(Enum.AssignRequestStatus.Approved.getAssignRequestStatusCode());
            //get task start in date range request
            List<TaskSummary> taskSummaries = taskRepository.findAllByExecutorAndStatusAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(userEntity, "Working", leaveRequest.getFromDate().atStartOfDay(), leaveRequest.getToDate().plusDays(1).atStartOfDay());
            //get task strat before but still not end
            taskSummaries.addAll(taskRepository.findAllByExecutorAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(userEntity, "Working", leaveRequest.getFromDate().atStartOfDay(),leaveRequest.getFromDate().plusDays(1).atStartOfDay()));
            //get all assign request approved
            List<AssignRequestSummary> assignRequestSummaries = assignRequestRepository.findAllByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(userEntity,integerList,leaveRequest.getFromDate(),leaveRequest.getToDate());
            List<Integer> assignedTaskSummaryIds = new ArrayList<>();
            for (AssignRequestSummary assignRequestSummary : assignRequestSummaries) {
                assignRequestSummary.getTasks().forEach(o -> assignedTaskSummaryIds.add(o.getId()));
            }
            taskSummaries.removeIf(taskSummary -> assignedTaskSummaryIds.contains(taskSummary.getId()));
            if(taskSummaries.size() > 0){
                return false;
            }
        }
        leaveRequest.setUser(user);
        return true;
    }

    private static File getResourceFile(String fileName) throws IOException {
//        ClassLoader classloader = ClassLoader.getSystemClassLoader();
//        return new File(classloader.getResource(fileName).getFile());
        return new ClassPathResource(fileName).getFile();
    }

    @Override
    public LeaveRequest save(LeaveRequest model) {

        // check user exists
        if (userRepository.existsById(model.getUser().getId()) == false) {
            throw new UserNotFoundException(model.getUser().getId());
        }
        else if (userRepository.existsById(model.getApprover().getId()) == false) {
            throw new UserNotFoundException(model.getApprover().getId());
        }

        LeaveRequestEntity leaveRequestEntity = ModelConverter.leaveRequestModelToEntity(model);
        LeaveRequestEntity savedLeaveRequestEntity = leaveRequestRepository.save(leaveRequestEntity);
        LeaveRequest savedLeaveRequest = ModelConverter.leaveRequestEntityToModel(savedLeaveRequestEntity);

        return savedLeaveRequest;
    }

    @Override
    public List<LeaveRequest> saveAll(List<LeaveRequest> models) {
        return null;
    }

    @Override
    public LeaveRequest findById(Integer id) {
        LeaveRequestEntity leaveRequestEntity = leaveRequestRepository.findById(id).orElseThrow(
                () -> new LeaveRequestNotFoundException(id)
        );
        return ModelConverter.leaveRequestEntityToModel(leaveRequestEntity);
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<LeaveRequest> findAll() {
        return null;
    }

    @Override
    public List<LeaveRequest> findAllById(List<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        leaveRequestRepository.deleteById(id);
    }

    @Override
    public void delete(LeaveRequest model) {

    }

    @Override
    public void deleteAll(List<LeaveRequest> models) {

    }

    @Override
    public void deleteAll() {

    }
}
