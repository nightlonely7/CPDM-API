package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.AssignRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AssignRequestRepository extends JpaRepository<AssignRequestEntity, Integer> {

    Page<AssignRequestSummary> findAllSummaryByUserAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    Page<AssignRequestSummary> findAllSummaryByApproverAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    boolean existsByUserAndStatusInAndFromDateGreaterThanEqualAndFromDateLessThanEqual(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    boolean existsByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate fromDate2);

    List<AssignRequestSummary> findAllByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    List<AssignRequestSummary> findAllByUserAndStatusInAndFromDateAfterAndFromDateLessThanEqual (UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    List<AssignRequestSummary> findAllByUserAndStatusInAndToDateGreaterThanEqualAndToDateBefore (UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    List<AssignRequestEntity> findAllByStatusAndFromDateLessThanEqualAndStartedFalseAndFinishedFalse(Integer status, LocalDate fromDate);

    List<AssignRequestEntity> findAllByStatusAndToDateLessThanEqualAndStartedTrueAndFinishedFalse(Integer status, LocalDate toDate);
}
