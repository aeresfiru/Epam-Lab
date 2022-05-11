package com.epam.esm.repository;

import com.epam.esm.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PasswordResetToken
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
