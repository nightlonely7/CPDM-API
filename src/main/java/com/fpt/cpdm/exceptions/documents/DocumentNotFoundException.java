package com.fpt.cpdm.exceptions.documents;

import com.fpt.cpdm.exceptions.EntityNotFoundException;

public class DocumentNotFoundException extends EntityNotFoundException {

    private static final String ENTITY = "Document";

    public DocumentNotFoundException(Integer id) {
        super(id, ENTITY);
    }

}
