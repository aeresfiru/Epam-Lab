package com.epam.esm.repository.impl;

import com.epam.esm.domain.User;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.builder.QueryBuilder;
import org.springframework.stereotype.Repository;

/**
 * UserRepositoryImpl
 *
 * @author alex
 * @version 1.0
 * @since 18.04.22
 */
@Repository
public class UserRepositoryImpl extends AbstractRepository<User>
        implements UserRepository {

    protected UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    protected QueryBuilder<User> getQueryBuilder() {
        throw new UnsupportedOperationException("Operation unsupported");
    }
}
