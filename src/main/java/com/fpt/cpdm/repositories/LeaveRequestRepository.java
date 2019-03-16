package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.LeaveRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity,Integer> {

    Page<LeaveRequestSummary> findAllSummaryByUserOrderByCreatedDateDesc(UserEntity userEntity, Pageable pageable);

    Page<LeaveRequestSummary> findAllSummaryByApproverOrderByCreatedDateDesc(UserEntity userEntity, Pageable pageable);
}
