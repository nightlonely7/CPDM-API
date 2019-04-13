package com.fpt.cpdm.services;

import com.fpt.cpdm.models.PushNotification;
import com.fpt.cpdm.models.notifications.Notification;
import com.fpt.cpdm.models.notifications.NotificationSummary;
import com.fpt.cpdm.models.users.User;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService extends CRUDService<Notification>{
    Page<NotificationSummary> findAllByUserOrderByCreatedTimeDesc(User user, Pageable pageable);

    void pushNotification(PushNotification pushNotification) throws JSONException;
}
