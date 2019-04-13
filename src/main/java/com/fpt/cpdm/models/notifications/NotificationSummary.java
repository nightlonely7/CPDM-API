package com.fpt.cpdm.models.notifications;

import com.fpt.cpdm.models.users.UserDisplayName;
import lombok.Data;

import java.time.LocalDateTime;

public interface NotificationSummary {
    Integer getId();

    String getTitle();

    String getDetail();

    String getHidden();

    String getUrl();

    LocalDateTime getCreatedTime();

    UserDisplayName getUser();

    UserDisplayName getCreator();
}
