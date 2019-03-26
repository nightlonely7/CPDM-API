package com.fpt.cpdm.models.leaveRequests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Leave {

    private LocalDate date;

    private boolean isWaiting;

    private boolean isApproved;
}
