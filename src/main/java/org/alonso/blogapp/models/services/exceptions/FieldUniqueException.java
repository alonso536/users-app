package org.alonso.blogapp.models.services.exceptions;

import lombok.Getter;

@Getter
public class FieldUniqueException extends RuntimeException {
    private String field;

    public FieldUniqueException(String field, String message) {
        super(message);
        this.field = field;
    }
}
