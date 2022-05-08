package com.epam.esm.rest;

import com.epam.esm.service.DuplicateEntityException;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.util.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Locale;

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

    private static final String REQUEST_INCORRECT_VALUE_MESSAGE = "request.incorrect-value";
    private static final String REQUEST_INCORRECT_PARAM_MESSAGE = "request.incorrect-param";
    private static final String REQUEST_INCORRECT_BODY_MESSAGE = "request.incorrect-body";
    private static final String METHOD_NOT_SUPPORTED_MESSAGE = "method-not_supported";

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex,
                                                  WebRequest request,
                                                  Locale locale) {

        log.warn("ResourceNotFoundException occurred: ", ex);
        String[] messages =
                new String[]{this.getLocalizedMessage(ex.getMessageKey(), locale) + ", id: (" + ex.getId() + ")."};
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(),
                messages, request.getDescription(false));
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage duplicateEntityException(DuplicateEntityException ex,
                                                 WebRequest request,
                                                 Locale locale) {
        log.warn("DuplicateEntityException occurred: ", ex);
        String[] messages =
                new String[]{this.getLocalizedMessage(ex.getMessageKey(), locale) + ", name: '" + ex.getField() + "'."};
        return new ErrorMessage(HttpStatus.CONFLICT.value(), new Date(),
                messages, request.getDescription(false));
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessage handleIllegalAccess(IllegalAccessException ex,
                                            WebRequest request,
                                            Locale locale) {
        log.warn("IllegalAccessException occurred: ", ex);
        String[] messages =
                new String[]{this.getLocalizedMessage(ex.getMessage(), locale)};
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(), new Date(),
                messages, request.getDescription(false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage anyOtherException(Exception ex,
                                          WebRequest request) {
        log.warn("Exception occurred: ", ex);
        String[] messages = new String[]{ex.getLocalizedMessage()};
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date(),
                messages, request.getDescription(false));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String[] message = ex.getBindingResult().getFieldErrors().stream()
                .map(c -> String.format("%s %s", c.getField(), c.getDefaultMessage()))
                .toArray(String[]::new);

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
                message, request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = messageSource.getMessage(REQUEST_INCORRECT_VALUE_MESSAGE,
                new Object[]{ex.getValue()}, LocaleContextHolder.getLocale());

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
                new String[]{message}, request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String[] message = ex.getBindingResult().getFieldErrors().stream()
                .map(c -> messageSource.getMessage(
                        REQUEST_INCORRECT_PARAM_MESSAGE,
                        new Object[]{c.getRejectedValue(), c.getField()},
                        LocaleContextHolder.getLocale()))
                .toArray(String[]::new);

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
                message, request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = messageSource.getMessage(REQUEST_INCORRECT_BODY_MESSAGE, null,
                LocaleContextHolder.getLocale());

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
                new String[]{message}, request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = messageSource.getMessage(METHOD_NOT_SUPPORTED_MESSAGE, null,
                LocaleContextHolder.getLocale());

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), new Date(),
                new String[]{message}, request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private String getLocalizedMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
