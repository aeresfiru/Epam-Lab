package com.epam.esm.controller.rest.handler;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

@Component
public class BindingResultParser {

    public String getFieldErrorMismatches(BindingResult bindingResult) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        bindingResult.getFieldErrors()
                .forEach(fieldError -> stringJoiner.add(fieldError.getField() + ": " + fieldError.getDefaultMessage()));
        return stringJoiner.toString();
    }
}
