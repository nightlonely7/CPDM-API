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

    Page<LeaveRequestSummary> findAllSummaryByApproverAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    List<LeaveRequestSummary> findAllSummaryByFromDateGreaterThanEqualOrToDateLessThanEqualAndUser(LocalDate toDate, LocalDate fromDate, UserEntity user);

    boolean existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(LocalDate fromdate, LocalDate toDate, UserEntity userEntity,Integer status);

    List<LeaveRequestSummary> findAllSummaryByUserAndStatusInAndFromDateGreaterThanEqualOrUserAndStatusInAndToDateLessThanEqual(UserEntity userEntity, List<Integer> integerList, LocalDate date, UserEntity userEntity2, List<Integer> integerList2, LocalDate date2);
}
