package com.fpt.cpdm.models.leaveRequests;

import lombok.Data;

import java.util.List;

@Data
public class LeaveSummary {
    private Integer dayOffPerYear;
    private Integer dayOffApproved;
    private Integer dayOffRemain;
    private List<LeaveRequestSummary> leaveRequestSummaries;
}
