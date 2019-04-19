package com.fpt.cpdm.models.notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.users.UserDisplayName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public interface NotificationSummary {
    Integer getId();

    String getTitle();

    String getDetail();

    boolean getHidden();

    boolean getRead();

    String getUrl();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreatedTime();

    UserDisplayName getUser();

    UserDisplayName getCreator();
}
