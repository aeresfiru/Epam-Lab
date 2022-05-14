package com.epam.esm.controller.rest.handler;

import com.epam.esm.service.DuplicateEntityException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName()
                    + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));

        String error = builder.toString();
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> entityNotFoundException(EntityNotFoundException ex) {
        String error = "Resource not found";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<Object> duplicateEntityException(DuplicateEntityException ex) {
        String error = "Duplicate entry " + ex.getField();
        HttpStatus status = HttpStatus.CONFLICT;
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        String error = ex.getLocalizedMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthenticationException(AccessDeniedException ex) {
        String error = ex.getLocalizedMessage();
        HttpStatus status = HttpStatus.FORBIDDEN;
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String error = "Error occurred";
        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), status);
    }
}
