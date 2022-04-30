package com.epam.esm.repository;

import com.epam.esm.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository
 *
 * @author alex
 * @version 1.0
 * @since 18.04.22
 */
public interface UserRepository {

    List<User> findAll(Pagination pagination);

    Optional<User> findById(long id);
}
