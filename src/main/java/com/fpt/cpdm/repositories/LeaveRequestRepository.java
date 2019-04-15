package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.LeaveRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity,Integer> {

    Page<LeaveRequestSummary> findAllSummaryByUserAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatus(UserEntity userEntity, Integer status);

    Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByApproverAndStatus(UserEntity userEntity, Integer status);

    boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromdate, LocalDate toDate, UserEntity userEntity,Integer status);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateGreaterThanEqualAndFromDateLessThanEqual(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate toDate);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateLessThanEqualAndToDateGreaterThanEqual(UserEntity userEntity, List<Integer> integerList, LocalDate fromDate, LocalDate fromDate2);
}
