package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.NotificationEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.notifications.NotificationSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {
    Page<NotificationSummary> findAllByUserOrderByCreatedTimeDesc(UserEntity userEntity, Pageable pageable);
}
