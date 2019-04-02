package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.AssignRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignRequestRepository extends JpaRepository<AssignRequestEntity, Integer> {

    Page<AssignRequestSummary> findAllSummaryByUserAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    Page<AssignRequestSummary> findAllSummaryByApproverAndStatus(UserEntity userEntity, Integer status, Pageable pageable);
}
