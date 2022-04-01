package com.epam.esm.dao;

import com.epam.esm.domain.Tag;

import java.util.List;

public interface TagDao {

    List<Tag> readAll();
}
