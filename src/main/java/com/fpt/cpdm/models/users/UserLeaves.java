package com.fpt.cpdm.models.users;

import com.fpt.cpdm.models.leaveRequests.Leave;
import lombok.Data;

import java.util.List;

@Data
public class UserLeaves {

    private String displayName;

    private List<Leave> leaveList;
}
