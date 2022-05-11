package com.epam.esm.rest;

import com.epam.esm.domain.User;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.CredentialsResponse;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthRestController
 *
 * @author alex
 * @version 1.0
 * @since 9.05.22
 */
@RestController
@AllArgsConstructor
public class AuthenticationRestController {

    //private final AuthenticationManager authenticationManager;

    //private final UserService userService;

    //private final JwtTokenProvider jwtTokenProvider;

    /*@PostMapping("/login")
    public CredentialsResponse login(@RequestBody AuthenticationDto authenticationDto) {
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
    }*/
}
