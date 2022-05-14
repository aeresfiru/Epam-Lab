package com.epam.esm.rest;

import com.epam.esm.domain.User;
import com.epam.esm.model.AuthenticationDto;
import com.epam.esm.model.CredentialsResponse;
import com.epam.esm.model.UserModel;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.AuthService;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * RegistrationController
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@RestController
@AllArgsConstructor
@Slf4j
public class AuthenticationRestController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public CredentialsResponse login(@RequestBody AuthenticationDto authenticationDto) {
        try {
            String username = authenticationDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    authenticationDto.getPassword())
            );
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username:" + username + ", not found");
            }
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            return new CredentialsResponse(username, token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Wrong username or password");
        }
    }

    @PostMapping("/signup")
    public CredentialsResponse signup(@RequestBody UserModel userModel) {
        User user = modelMapper.map(userModel, User.class);
        userService.signup(user);

    }

}
