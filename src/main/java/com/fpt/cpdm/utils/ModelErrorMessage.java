package com.fpt.cpdm.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ModelErrorMessage {

    public static String build(BindingResult result) {
        List<FieldError> fieldsErrors = result.getFieldErrors();
        StringBuilder messageBuilder = new StringBuilder();
        for (FieldError fieldError : fieldsErrors) {
            messageBuilder
                    .append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append(" as ")
                    .append("'")
                    .append(fieldError.getRejectedValue())
                    .append("'")
                    .append(" \n ");
        }
        String message = messageBuilder.toString();
        return message;
    }
}
