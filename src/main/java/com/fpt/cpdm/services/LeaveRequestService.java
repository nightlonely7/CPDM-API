package com.fpt.cpdm.services;

import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestService extends CRUDService<LeaveRequest> {

    Page<LeaveRequestSummary> findAllSummaryByUserAndStatus(User userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatus(User userEntity, Integer status);

    Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User userEntity, Integer status);

    boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromDate, LocalDate toDate, User user,Integer status);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateGreaterThanEqualAndFromDateLessThanEqual(User user, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateLessThanAndToDateGreaterThanEqual(User user, List<Integer> integerList, LocalDate fromDate);

    List<LeaveRequest> findAllByUserAndStatusInAndFromDateGreaterThanEqualAndFromDateLessThanEqual(User user, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    List<LeaveRequest> findAllByUserAndStatusInAndFromDateLessThanAndToDateGreaterThanEqual(User user, List<Integer> integerList, LocalDate fromDate);

    boolean validateNewLeaveRequest(User user, LeaveRequest leaveRequest);
}
