package com.epam.esm.repository;

import com.epam.esm.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * VerificationTokenRepository
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
