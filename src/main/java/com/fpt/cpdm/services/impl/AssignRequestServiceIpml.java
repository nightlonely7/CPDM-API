package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.AssignRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.assignRequests.AssignRequestNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.AssignRequestRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.AssignRequestService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignRequestServiceIpml implements AssignRequestService {
    private AssignRequestRepository assignRequestRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Autowired
    public AssignRequestServiceIpml(AssignRequestRepository assignRequestRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.assignRequestRepository = assignRequestRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public AssignRequest save(AssignRequest model) {

        // check user exists
        if (userRepository.existsById(model.getUser().getId()) == false) {
            throw new UserNotFoundException(model.getUser().getId());
        } else if(userRepository.existsById(model.getAssignee().getId()) == false){
            throw new UserNotFoundException(model.getAssignee().getId());
        }
        else if (userRepository.existsById(model.getApprover().getId()) == false) {
            throw new UserNotFoundException(model.getApprover().getId());
        }

        //check task exists
        model.getTasks().forEach(task -> {
            if(taskRepository.existsById(task.getId()) == false){
                throw new TaskNotFoundException(task.getId());
            }
        });

        AssignRequestEntity assignRequestEntity = ModelConverter.assignRequestModelToEntity(model);
        AssignRequestEntity savedAssignRequestEntity = assignRequestRepository.save(assignRequestEntity);
        AssignRequest savedAssignRequest = ModelConverter.assignRequestEntityToModel(savedAssignRequestEntity);

        return savedAssignRequest;
    }

    @Override
    public List<AssignRequest> saveAll(List<AssignRequest> models) {
        return null;
    }

    @Override
    public AssignRequest findById(Integer id) {
        AssignRequestEntity assignRequestEntity = assignRequestRepository.findById(id).orElseThrow(
                () -> new AssignRequestNotFoundException(id)
        );

        return ModelConverter.assignRequestEntityToModel(assignRequestEntity);
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<AssignRequest> findAll() {
        return null;
    }

    @Override
    public List<AssignRequest> findAllById(List<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        assignRequestRepository.deleteById(id);
    }

    @Override
    public void delete(AssignRequest model) {

    }

    @Override
    public void deleteAll(List<AssignRequest> models) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Page<AssignRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<AssignRequestSummary> assignRequestSummaries = assignRequestRepository.findAllSummaryByUserAndStatus(userEntity,status,pageable);
        return assignRequestSummaries;
    }

    @Override
    public Page<AssignRequestSummary> findAllSummaryByApproverAndStatus(User approver, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(approver);
        Page<AssignRequestSummary> assignRequestSummaries = assignRequestRepository.findAllSummaryByApproverAndStatus(userEntity, status, pageable);
        return assignRequestSummaries;
    }
}
