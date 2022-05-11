package com.epam.esm.repository;

import com.epam.esm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 *
 * @author alex
 * @version 1.0
 * @since 7.05.22
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByEmail(String email);

    User findByUsername(String username);
}
