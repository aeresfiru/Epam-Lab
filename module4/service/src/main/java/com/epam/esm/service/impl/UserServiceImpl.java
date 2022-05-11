package com.epam.esm.service.impl;

import com.epam.esm.domain.*;
import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.repository.PasswordResetTokenRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.VerificationTokenRepository;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * UserServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 25.04.22
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(long id) {
        log.info("IN findById - userId: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.info("IN findAll - pageable: {}", pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public User registerUser(User user) {
        log.info("IN registerUser");
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(role));
        user.setStatus(Status.NOT_ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userRepository.save(user);
        log.info("IN registerUser - user successfully registered");
        return registeredUser;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        log.info("IN saveVerificationTokenForUser - token: {}, user: {}", token, user);
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        log.info("IN validateVerificationToken - token: {}", token);
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "invalid";
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        log.info("IN generateNewVerificationToken - oldToken: {}", oldToken);
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        if (verificationToken == null) {
            throw new ResourceNotFoundException(ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        verificationToken.setToken(UUID.randomUUID().toString());
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public User findUserByEmail(String email) {
        log.info("IN findUserByEmail - email: {}", email);
        return userRepository.findUserByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        log.info("IN createPasswordResetTokenForUser - user: {}, token: {}", user, token);
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        log.info("IN validatePasswordResetToken - token: {}", token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return "invalid";
        }

        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        log.info("IN getUserByPasswordResetToken - token: {}", token);
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("IN changePassword - password changed");
    }

    @Override
    public boolean validateOldPassword(User user, String oldPassword) {
        log.info("IN validateOldPassword - validating old password");
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public User findByUsername(String username) {
        log.info("IN findByUsername - username: {}", username);
        return userRepository.findByUsername(username);
    }
}
