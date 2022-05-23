package com.epam.esm.controller.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * RestAccessDeniedHandler
 *
 * @author alex
 * @version 1.0
 * @since 14.05.22
 */
@Component
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException ex)
            throws IOException, ServletException {

        HttpStatus status = HttpStatus.FORBIDDEN;
        PrintWriter out = resp.getWriter();
        String error = getErrorMessage(req);

        ApiError apiError = new ApiError(new Date(), status.value(), status.name(), ex.getMessage(), error);

        resp.setStatus(status.value());
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(objectMapper.writeValueAsString(apiError));
    }

    private String getErrorMessage(HttpServletRequest req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return "User: '" + auth.getName()
                    + "' attempted to access the protected URL: "
                    + req.getRequestURI();
        }
        return "User attempted to access the protected URL: " + req.getRequestURI();
    }
}
