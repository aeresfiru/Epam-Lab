package com.epam.esm.controller.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ErrorMessage
 *
 * @author alex
 * @version 1.0
 * @since 28.04.22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String[] messages;
    private String description;
}
