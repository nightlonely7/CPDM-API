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

    Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByFromDateGreaterThanEqualOrToDateLessThanEqualAndUser(LocalDate toDate, LocalDate fromDate, User user);

    boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromDate, LocalDate toDate, User user,Integer status);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndToday(User user, List<Integer> integerList, LocalDate date);
}
