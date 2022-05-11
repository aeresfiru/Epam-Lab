package com.epam.esm.service;

import com.epam.esm.domain.User;
import com.epam.esm.domain.VerificationToken;
import com.epam.esm.dto.UserRegistrationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * UserService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface UserService {

    User findById(long id);

    Page<User> findAll(Pageable pageable);

    User registerUser(User user);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User user, String newPassword);

    boolean validateOldPassword(User user, String oldPassword);

    User findByUsername(String username);
}
