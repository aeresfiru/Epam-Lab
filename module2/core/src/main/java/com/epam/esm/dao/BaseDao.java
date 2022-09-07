package com.epam.esm.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseDao<T, K> {

    T create(T t);

    List<T> readAll();

    Optional<T> readById(K id);

    T update(T t);

    void delete(K id);
}
