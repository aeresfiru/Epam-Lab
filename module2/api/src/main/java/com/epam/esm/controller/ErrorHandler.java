package com.epam.esm.controller;

import com.epam.esm.service.exception.DuplicateEntityException;
import com.epam.esm.service.exception.EntityNotFoundException;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.IncorrectParameterException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
@AllArgsConstructor
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final HttpErrorResponse incorrectParameterException(IncorrectParameterException ex, Locale locale) {
        logger.warn("Incorrect parameter: " + ex.getFieldName() + ", value: " + ex.getValue());
        String message = messageSource.getMessage(ErrorConstraint.INCORRECT_PARAMETER,
                new Object[]{ex.getFieldName(), ex.getValue()}, locale);
        return new HttpErrorResponse(message, ex.getErrorCode(), ex.toString());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final HttpErrorResponse handleEntityNotFoundException(EntityNotFoundException ex, Locale locale) {
        logger.warn(ex.getId());
        String message = messageSource.getMessage(ErrorConstraint.RESOURCE_NOT_FOUND,
                new Object[]{ex.getId()}, locale);
        return new HttpErrorResponse(message, ex.getErrorCode(), ex.toString());
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public final HttpErrorResponse duplicateEntityException(DuplicateEntityException ex, Locale locale) {
        logger.warn(ex.getField());
        String message = messageSource.getMessage(ErrorConstraint.CONFLICT,
                new Object[]{ex.getField()}, locale);
        return new HttpErrorResponse(message, ex.getErrorCode(), ex.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final HttpErrorResponse exception(Exception ex, Locale locale) {
        log.error(ex.getMessage());
        String message = messageSource.getMessage(ErrorConstraint.INTERNAL,
                null, locale);
        return new HttpErrorResponse(message, ErrorCode.INTERNAL_SERVER_ERROR, ex.toString());
    }
}
