package com.fpt.cpdm.models.notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.users.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notification {
    @EqualsAndHashCode.Include
    private Integer id;
    private String title;
    private String detail;
    private boolean isHidden;
    private String url;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    private User user;
    private User creator;
}
