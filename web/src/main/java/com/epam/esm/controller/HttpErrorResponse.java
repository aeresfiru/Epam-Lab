package com.epam.esm.controller;

import lombok.Data;

@Data
public class HttpErrorResponse {
    private String message;
    private String errorCode;

    public HttpErrorResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
