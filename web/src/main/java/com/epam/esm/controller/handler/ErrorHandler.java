package com.epam.esm.controller.handler;

import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ErrorConstraint;
import com.epam.esm.service.exception.IncorrectParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final String NOT_FOUND_ERROR_CODE = "404";
    private static final String CONFLICT_ERROR_CODE = "409";
    private static final String BAD_REQUEST_ERROR_CODE = "400";
    private final MessageSource messageSource;

    @Autowired
    ErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final HttpErrorResponse handleIncorrectParameterExceptions(IncorrectParameterException ex, Locale locale) {
        String message = this.getLocalizedMessage(ex.getErrorMessage(), locale);
        String errorCode = BAD_REQUEST_ERROR_CODE + ex.getErrorCode();
        return new HttpErrorResponse(message, errorCode);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final HttpErrorResponse handleEntityNotFoundExceptions(EntityNotFoundException ex, Locale locale) {
        String message = this.getLocalizedMessage(ErrorConstraint.RESOURCE_NOT_FOUND, locale);
        message = String.format(message, ex.getId());
        String errorCode = NOT_FOUND_ERROR_CODE + ex.getCode();
        return new HttpErrorResponse(message, errorCode);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final HttpErrorResponse handleDuplicateEntityExceptions(DuplicateEntityException ex, Locale locale) {
        String message = this.getLocalizedMessage(ErrorConstraint.CONFLICT, locale);
        message = String.format(message, ex.getName());
        String errorCode = CONFLICT_ERROR_CODE + ex.getCode();
        return new HttpErrorResponse(message, errorCode);
    }

    private String getLocalizedMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
