package com.fpt.cpdm.exceptions.comments;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "Comment";

    public CommentNotFoundException(Integer id) {
        super(id, ENTITY);
    }
}
