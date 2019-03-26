package com.fpt.cpdm.models.leaveRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.IdOnly;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface LeaveRequestSummary {

    Integer getId();

    String getContent();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getFromDate();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getToDate();

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate getCreatedDate();

    Integer getStatus();

    UserDisplayName getUser();

    UserDisplayName getApprover();
}
