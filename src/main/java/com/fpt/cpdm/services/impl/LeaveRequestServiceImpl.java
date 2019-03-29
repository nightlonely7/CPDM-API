package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.LeaveRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.leaveRequests.LeaveRequestNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.LeaveRequestRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.LeaveRequestService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private LeaveRequestRepository leaveRequestRepository;
    private UserRepository userRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, UserRepository userRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<LeaveRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestRepository.findAllSummaryByUserAndStatus(userEntity, status, pageable);
        return leaveRequestSummaries;
    }

    @Override
    public Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User approver, Integer status, Pageable pageable) {
        UserEntity userEntity = ModelConverter.userModelToEntity(approver);
        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestRepository.findAllSummaryByApproverAndStatus(userEntity, status, pageable);
        return leaveRequestSummaries;
    }

    @Override
    public boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromDate, LocalDate toDate, User user, Integer status) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return leaveRequestRepository.existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(fromDate,toDate,userEntity,status);
    }

    @Override
    public List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateIsBetween(User user, List<Integer> integerList, LocalDate fromDate, LocalDate toDate) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return leaveRequestRepository.findAllSummaryByUserAndStatusInAndFromDateIsBetween(userEntity, integerList, fromDate, toDate);
    }

    @Override
    public List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(User user, List<Integer> integerList, LocalDate fromDate) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return leaveRequestRepository.findAllSummaryByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(userEntity, integerList, fromDate, fromDate);
    }

    @Override
    public LeaveRequest save(LeaveRequest model) {

        // check user exists
        if (userRepository.existsById(model.getUser().getId()) == false) {
            throw new UserNotFoundException(model.getUser().getId());
        }
        else if (userRepository.existsById(model.getUser().getId()) == false) {
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
