package com.fpt.cpdm.services;

import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveRequestService extends CRUDService<LeaveRequest> {

    Page<LeaveRequestSummary> findAllSummaryByUserOrderByCreatedDateDesc(User userEntity, Pageable pageable);

    Page<LeaveRequestSummary> findAllSummaryByApproverOrderByCreatedDateDesc(User userEntity, Pageable pageable);
}
