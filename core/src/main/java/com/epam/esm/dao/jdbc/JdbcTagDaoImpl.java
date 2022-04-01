package com.epam.esm.dao.jdbc;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcTagDaoImpl implements TagDao {

    private static final String SELECT_ALL_SQL = "SELECT id, name FROM tag;";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL,
                new BeanPropertyRowMapper<>(Tag.class));
    }
}
