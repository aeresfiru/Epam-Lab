package com.epam.esm.service;

public class EntityNotFoundException extends RuntimeException{
    private final long id;
    private final ErrorCode code;

    public EntityNotFoundException(long id, ErrorCode code) {
        this.id = id;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public ErrorCode getCode() {
        return code;
    }
}
