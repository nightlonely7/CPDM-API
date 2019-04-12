package com.fpt.cpdm.models.askingRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDate;
import java.util.List;

public interface AskingRequestSummary {
    Integer getId();

    String getContent();

    String getResponse();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getCreatedDate();

    Integer getStatus();

    UserDisplayName getUser();

    UserDisplayName getReceiver();

    List<TaskSummary> getTasks();
}
