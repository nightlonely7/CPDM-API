package com.fpt.cpdm.models.assignRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.time.LocalDate;
import java.util.List;

public interface AssignRequestSummary  {
    Integer getId();

    String getContent();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getFromDate();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getToDate();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getCreatedDate();

    Integer getStatus();

    Boolean getStarted();

    Boolean getFinished();

    UserDisplayName getUser();

    UserDisplayName getAssignee();

    UserDisplayName getApprover();

    List<TaskSummary> getTasks();
}
