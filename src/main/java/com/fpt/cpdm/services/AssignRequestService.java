package com.fpt.cpdm.services;

import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssignRequestService extends CRUDService<AssignRequest> {

    Page<AssignRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable);

    Page<AssignRequestSummary> findAllSummaryByApproverAndStatus(User approver, Integer status, Pageable pageable);
}
