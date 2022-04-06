package com.epam.esm.service.exception;

public class IncorrectParameterException extends RuntimeException {

    private final String errorMessage;
    private final ErrorCode errorCode;

    public IncorrectParameterException(String errorMessage, ErrorCode errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }
}
