package com.epam.esm.service.exception;

public class EntityNotFoundException extends RuntimeException {
    private final long id;
    private final ErrorCode code;

    public EntityNotFoundException(long id, ErrorCode code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code.getCode();
    }
}
