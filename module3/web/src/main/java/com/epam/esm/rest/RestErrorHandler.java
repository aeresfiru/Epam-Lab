package com.epam.esm.rest;

import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.IncorrectParameterException;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.util.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * ExceptionHandler
 *
 * @author alex
 * @version 1.0
 * @since 28.04.22
 */
@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage incorrectParameterException(IncorrectParameterException ex,
                                                    WebRequest request,
                                                    Locale locale) {
        log.warn("IncorrectParameterException occurred: ", ex);
        String[] messages = this.createErrorMessage(ex.getExceptionResult().getExceptions(), locale);
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                messages,
                request.getDescription(false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex,
                                                  WebRequest request,
                                                  Locale locale) {

        log.warn("ResourceNotFoundException occurred: ", ex);
        String[] messages =
                new String[]{this.getLocalizedMessage(ex.getMessageKey(), locale) + ", id: (" + ex.getId() + ")."};
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                messages,
                request.getDescription(false));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage duplicateEntityException(DuplicateEntityException ex,
                                                 WebRequest request,
                                                 Locale locale) {
        log.warn("DuplicateEntityException occurred: ", ex);
        String[] messages =
                new String[]{this.getLocalizedMessage(ex.getMessageKey(), locale) + ", name: '" + ex.getField() + "'."};
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                messages,
                request.getDescription(false));
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage illegalAccessException(IllegalAccessException ex,
                                               WebRequest request,
                                               Locale locale) {
        log.warn("IllegalAccessException occurred: ", ex);
        String[] messages =
                new String[]{this.getLocalizedMessage(ex.getMessage(), locale)};
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                messages,
                request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage anyOtherException(Exception ex,
                                          WebRequest request) {
        log.warn("Exception occurred: ", ex);
        String[] messages = new String[]{ex.getLocalizedMessage()};
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                messages,
                request.getDescription(false));
    }

    private String getLocalizedMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    private String[] createErrorMessage(Map<String, Object> exceptions, Locale locale) {
        return exceptions.entrySet().stream()
                .map(e -> this.getLocalizedMessage(e.getKey(), locale) + ": " + e.getValue())
                .toArray(String[]::new);
    }
}
