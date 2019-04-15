package com.fpt.cpdm.exceptions.notifications;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class NotificationNotFoundException extends EntityNotFoundException {
    private static final String ENTITY = "Notification";

    public NotificationNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
