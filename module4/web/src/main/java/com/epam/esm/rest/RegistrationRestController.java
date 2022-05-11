package com.epam.esm.rest;

import com.epam.esm.domain.PasswordResetToken;
import com.epam.esm.domain.User;
import com.epam.esm.domain.VerificationToken;
import com.epam.esm.dto.AuthenticationDto;
import com.epam.esm.dto.CredentialsResponse;
import com.epam.esm.dto.PasswordDto;
import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.event.RegistrationCompleteEvent;
import com.epam.esm.security.jwt.JwtTokenProvider;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

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
public class RegistrationRestController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final ApplicationEventPublisher publisher;

    private final JwtTokenProvider jwtTokenProvider;

    private final ModelMapper mapper;

    @PostMapping("/login")
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
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegistrationDto userDto, final HttpServletRequest req) {
        User user = userService.registerUser(mapper.map(userDto, User.class));
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(req)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "user verified successfully";
        } else {
            return "Bad user";
        }
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest req) {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        resendVerificationTokenMail(applicationUrl(req), verificationToken);
        return "Verification link sent";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordDto passwordDto, HttpServletRequest req) {
        User user = userService.findUserByEmail(passwordDto.getEmail());
        log.info("params: {}", passwordDto);
        String url = "";
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            url = passwordResetToken(applicationUrl(req), token);
        } else {
            log.info("User is null");
        }
        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordDto passwordDto) {

        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid token";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordDto.getNewPassword());
            return "Password Reset successful";
        } else {
            return "Invalid token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordDto passwordDto) {
        User user = userService.findUserByEmail(passwordDto.getEmail());
        if (!userService.validateOldPassword(user, passwordDto.getOldPassword())) {
            return "Invalid old password";
        }
        //save new password
        userService.changePassword(user, passwordDto.getNewPassword());
        return "Password change successfully";
    }

    private String passwordResetToken(String applicationUrl, String token) {
        //send mail to user
        String url =
                applicationUrl
                        + "/savePassword?token="
                        + token;

        //sendVerificationEmail()
        log.info("Link to reset password: {}", url);
        return url;
    }

    private void resendVerificationTokenMail(String applicationUrl, VerificationToken verificationToken) {
        //send mail to user
        String url =
                applicationUrl
                        + "/verifyRegistration?token="
                        + verificationToken.getToken();

        //sendVerificationEmail()
        log.info("Link to verify account: {}", url);
    }

    private String applicationUrl(HttpServletRequest req) {
        return "http://"
                + req.getServerName()
                + ":"
                + req.getServerPort()
                + req.getContextPath();
    }
}
