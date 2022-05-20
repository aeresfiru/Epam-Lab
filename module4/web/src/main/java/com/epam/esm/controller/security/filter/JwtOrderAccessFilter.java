package com.epam.esm.controller.security.filter;

import com.epam.esm.controller.security.JwtAuthenticationException;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtOrderAccessFilter
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Component
@AllArgsConstructor
@Slf4j
public class JwtOrderAccessFilter implements Filter {

    private static final String USERS = "/users/";

    private static final String ORDERS = "/orders/";

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final OrderService orderService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.info("IN doFilter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        if (uri.contains(ORDERS)) {
            log.info("In JwtOrderAccessFilter - user trying to access order, uri: {}", uri);

            User user = getUserFromRequest(req);
            boolean haveAccessToOrder = isUserHaveAccessToOrder(user, uri);

            if (!haveAccessToOrder) {
                throw new JwtAuthenticationException("User tries to access not owned order");
            }
        }
        chain.doFilter(req, resp);
    }

    private User getUserFromRequest(HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        String username = jwtTokenProvider.getUsername(token);
        return userService.findByUsername(username);
    }

    private boolean isUserHaveAccessToOrder(User user, String uri) {
        boolean isUserOrder = isUserOrder(user, uri);
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        return isUserOrder || isAdmin;
    }

    private boolean isUserOrder(User user, String uri) {
        if (uri.contains(USERS)) {
            return user.getId().equals(getUserIdFromUri(uri));
        }
        Order order = orderService.findById(getOrderIdFromUri(uri));
        return order.getUser().getId().equals(user.getId());
    }

    private Long getUserIdFromUri(String uri) {
        String part = uri.replace(USERS, "");
        String id = part.substring(0, part.indexOf('/'));
        return Long.valueOf(id);
    }

    private Long getOrderIdFromUri(String uri) {
        String id = uri.replace(ORDERS, "");
        return Long.valueOf(id);
    }
}
