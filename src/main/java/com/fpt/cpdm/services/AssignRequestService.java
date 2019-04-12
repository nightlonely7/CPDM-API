package com.fpt.cpdm.services;

import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AssignRequestService extends CRUDService<AssignRequest> {

    Page<AssignRequestSummary> findAllSummaryByUserAndStatus(User user, Integer status, Pageable pageable);

    Page<AssignRequestSummary> findAllSummaryByApproverAndStatus(User approver, Integer status, Pageable pageable);

    List<AssignRequestSummary> findAllSummaryByTaskAndDateRange(User user, Integer taskId, LocalDate fromDate, LocalDate toDate, List<Integer> statusList);
}
