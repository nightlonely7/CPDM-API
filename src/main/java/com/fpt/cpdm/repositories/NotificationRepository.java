package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.NotificationEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.notifications.NotificationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {
    List<NotificationSummary> findAllByUserAndHiddenOrderByCreatedTimeDesc(UserEntity userEntity, boolean hidden);
}
