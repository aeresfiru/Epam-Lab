package com.epam.esm.controller.rest.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

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

    private HttpStatus status;

    private Integer code;

    private String message;
}
