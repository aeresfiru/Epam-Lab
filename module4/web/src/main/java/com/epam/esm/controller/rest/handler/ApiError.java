package com.epam.esm.controller.rest.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ApiError
 *
 * @author alex
 * @version 1.0
 * @since 14.05.22
 */
@Data
@AllArgsConstructor
public class ApiError {

    private Date timestamp;

    private Integer code;

    private String status;
    private String message;
    private List<String> errors;

    public ApiError(Date timestamp, Integer code, String status, String message, String error) {
        super();
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
