package com.fpt.cpdm.models.comments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.IdOnly;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface CommentSummary {
    
    Integer getId();

    UserDisplayName getUser();

    IdOnly getTask();

    String getContent();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getCreatedDate();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getLastModifiedDate();

    Integer getStatus();
}
