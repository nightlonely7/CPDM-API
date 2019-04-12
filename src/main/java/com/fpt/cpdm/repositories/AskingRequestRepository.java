package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.AskingRequestEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.askingRequests.AskingRequestSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AskingRequestRepository extends JpaRepository<AskingRequestEntity, Integer> {

    Page<AskingRequestSummary> findAllByUserAndStatus(UserEntity userEntity, Integer status, Pageable pageable);

    Page<AskingRequestSummary> findAllByReceiverAndStatus(UserEntity userEntity, Integer status, Pageable pageable);
}
