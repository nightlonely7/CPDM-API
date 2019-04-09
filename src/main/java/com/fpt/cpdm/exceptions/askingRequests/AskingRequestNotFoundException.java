package com.fpt.cpdm.exceptions.askingRequests;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class AskingRequestNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "AskingRequest";

    public AskingRequestNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
