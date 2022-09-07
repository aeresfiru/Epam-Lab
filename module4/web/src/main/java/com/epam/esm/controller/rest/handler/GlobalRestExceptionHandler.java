package com.epam.esm.controller.rest.handler;

import com.epam.esm.service.DuplicateEntityException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

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
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.warn("Method argument not valid, ", ex);
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        log.warn("Missing servlet request parameter, ", ex);
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation, ", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Method argument type mismatch, ", ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        log.warn("No handler found, ", ex);
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        log.warn("Request method not supported, ", ex);
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn("Entity not found, ", ex);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicateEntityException(DuplicateEntityException ex) {
        log.warn("Duplicate entity, ", ex);
        HttpStatus status = HttpStatus.CONFLICT;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Authentication exception, ", ex);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied, ", ex);
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAll(Exception ex) {
        log.warn("Unhandled exception, ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, status);
    }
}
