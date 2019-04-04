package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.AssignRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AssignRequestRepository extends JpaRepository<AssignRequestEntity, Integer> {

    Page<AssignRequestSummary> findAllSummaryByUserAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    Page<AssignRequestSummary> findAllSummaryByApproverAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    boolean existsByUserAndStatusInAndFromDateIsBetween(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    boolean existsByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate fromDate2);
}
