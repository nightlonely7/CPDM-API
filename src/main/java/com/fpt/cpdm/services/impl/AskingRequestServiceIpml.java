package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.AskingRequestEntity;
import com.fpt.cpdm.entities.AssignRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.askingRequests.AskingRequestNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.askingRequests.AskingRequest;
import com.fpt.cpdm.models.askingRequests.AskingRequestSummary;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.AskingRequestRepository;
import com.fpt.cpdm.repositories.AssignRequestRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.AskingRequestService;
import com.fpt.cpdm.services.AssignRequestService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AskingRequestServiceIpml implements AskingRequestService {
    private AskingRequestRepository askingRequestRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    @Autowired
    public AskingRequestServiceIpml(AskingRequestRepository askingRequestRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.askingRequestRepository = askingRequestRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public Page<AskingRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return askingRequestRepository.findAllByUserAndStatus(userEntity,status,pageable);
    }

    @Override
    public Page<AskingRequestSummary> findAllSummaryByReceiverAndStatus(User receiver, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(receiver);
        return askingRequestRepository.findAllByReceiverAndStatus(userEntity,status,pageable);
    }

    @Override
    public AskingRequest save(AskingRequest model) {
        // check user exists
        if (userRepository.existsById(model.getUser().getId()) == false) {
            throw new UserNotFoundException(model.getUser().getId());
        }
        else if (userRepository.existsById(model.getReceiver().getId()) == false) {
            throw new UserNotFoundException(model.getReceiver().getId());
        }

        //check task exists
        model.getTasks().forEach(task -> {
            if(taskRepository.existsById(task.getId()) == false){
                throw new TaskNotFoundException(task.getId());
            }
        });

        AskingRequestEntity askingRequestEntity = ModelConverter.askingRequestModelToEntity(model);
        AskingRequestEntity savedAssignRequestEntity = askingRequestRepository.save(askingRequestEntity);
        AskingRequest savedAssignRequest = ModelConverter.askingRequestEntityToModel(savedAssignRequestEntity);

        return savedAssignRequest;
    }

    @Override
    public List<AskingRequest> saveAll(List<AskingRequest> models) {
        return null;
    }

    @Override
    public AskingRequest findById(Integer id) {
        AskingRequestEntity askingRequestEntity = askingRequestRepository.findById(id).orElseThrow(
                () -> new AskingRequestNotFoundException(id)
        );

        return ModelConverter.askingRequestEntityToModel(askingRequestEntity);
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<AskingRequest> findAll() {
        return null;
    }

    @Override
    public List<AskingRequest> findAllById(List<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        askingRequestRepository.deleteById(id);
    }

    @Override
    public void delete(AskingRequest model) {

    }

    @Override
    public void deleteAll(List<AskingRequest> models) {

    }

    @Override
    public void deleteAll() {

    }
}
