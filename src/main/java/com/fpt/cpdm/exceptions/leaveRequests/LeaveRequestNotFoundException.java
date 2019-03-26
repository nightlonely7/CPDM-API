package com.fpt.cpdm.exceptions.leaveRequests;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class LeaveRequestNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "LeaveRequest";

    public LeaveRequestNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
