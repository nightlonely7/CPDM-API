package com.fpt.cpdm.models.leaveRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.IdOnly;

import java.time.LocalDateTime;

public interface LeaveRequestSummary {

    Integer getId();

    String getContent();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getFromDate();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getToDate();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getCreatedDate();

    Integer getStatus();

    IdOnly getUser();

    IdOnly getApprover();
}
