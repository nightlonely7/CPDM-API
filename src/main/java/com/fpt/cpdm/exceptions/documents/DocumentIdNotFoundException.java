package com.fpt.cpdm.exceptions.documents;

import com.fpt.cpdm.exceptions.EntityIdNotFoundException;

public class DocumentIdNotFoundException extends EntityIdNotFoundException {

    private static final String ENTITY = "Document";

    public DocumentIdNotFoundException(Integer id) {
        super(id, ENTITY);
    }

}
