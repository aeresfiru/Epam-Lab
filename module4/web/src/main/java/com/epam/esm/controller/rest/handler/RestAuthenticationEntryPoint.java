package com.epam.esm.controller.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * RestAuthenticationEntryPoint
 *
 * @author alex
 * @version 1.0
 * @since 14.05.22
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ERROR = "Access is denied";
    private final ObjectMapper objectMapper;

    @Autowired
    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex)
            throws IOException, ServletException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;
        PrintWriter out = resp.getWriter();

        ApiError apiError = new ApiError(status, status.value(), ex.getLocalizedMessage());

        resp.setStatus(status.value());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(objectMapper.writeValueAsString(apiError));
    }
}
