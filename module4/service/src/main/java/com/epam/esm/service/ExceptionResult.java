package com.epam.esm.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * ExceptionResult
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@Data
@AllArgsConstructor
public class ExceptionResult {

    private final Map<String, Object> exceptions;

    public void addException(String msg, Object field) {
        this.exceptions.put(msg, field);
    }
}
