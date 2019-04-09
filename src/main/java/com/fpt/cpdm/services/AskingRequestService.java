package com.fpt.cpdm.services;

import com.fpt.cpdm.models.askingRequests.AskingRequest;
import com.fpt.cpdm.models.askingRequests.AskingRequestSummary;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AskingRequestService extends CRUDService<AskingRequest> {

    Page<AskingRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable);

    Page<AskingRequestSummary> findAllSummaryByReceiverAndStatus(User approver, Integer status, Pageable pageable);
}
