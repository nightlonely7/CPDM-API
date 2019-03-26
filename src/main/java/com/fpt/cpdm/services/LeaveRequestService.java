package com.fpt.cpdm.services;

import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface LeaveRequestService extends CRUDService<LeaveRequest> {

    Page<LeaveRequestSummary> findAllSummaryByUserAndStatus(User userEntity, Integer status, Pageable pageable);

    Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(User userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByFromDateGreaterThanEqualOrToDateLessThanEqualAndUser(LocalDate toDate, LocalDate fromDate, User user);

    boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromDate, LocalDate toDate, User user,Integer status);
}
