package com.epam.esm.controller.rest;

import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import com.epam.esm.domain.User;
import com.epam.esm.service.AuthService;
import com.epam.esm.service.model.AuthenticationModel;
import com.epam.esm.service.model.JwtAuthResponse;
import com.epam.esm.service.model.UserRegistrationModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * RegistrationController
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@RestController("/api/v1")
@AllArgsConstructor
@Slf4j
@PreAuthorize("!hasRole('ROLE_ADMIN') && !hasRole('ROLE_USER')")
public class AuthRestController {

    private final AuthService authService;

    private final ModelMapper modelMapper;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public JwtAuthResponse login(@RequestBody AuthenticationModel authenticationModel) {
        User user = authService.login(authenticationModel);
        return createJwtAuthResponse(user);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid UserRegistrationModel userModel) {
        log.info("IN signup");
        User user = modelMapper.map(userModel, User.class);
        authService.signup(user);
    }

    private JwtAuthResponse createJwtAuthResponse(User user) {
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        return new JwtAuthResponse(user.getUsername(), token);
    }
}
