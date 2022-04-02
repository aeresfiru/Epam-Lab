package com.epam.esm.dao.jdbc;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTagDaoImpl implements TagDao {

    private static final String CREATE_ONE_SQL =
            "INSERT INTO tag(name) VALUES (?);";

    private static final String SELECT_ALL_SQL =
            "SELECT tag.id, tag.name FROM tag;";

    private static final String SELECT_ONE_SQL =
            "SELECT tag.id, tag.name FROM tag WHERE tag.id = ?;";

    private static final String DELETE_ONE_SQL =
            "DELETE FROM tag WHERE tag.id = ?;";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcTagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    @Override
    public boolean create(Tag tag) {
        simpleJdbcInsert.withTableName("tag").usingGeneratedKeyColumns("id");
        Number id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(tag));
        tag.setId(id.longValue());
        return jdbcTemplate.update(CREATE_ONE_SQL, tag.getName()) == 1;
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL,
                new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> read(long id) {
        List<Tag> tags = jdbcTemplate.query(SELECT_ONE_SQL,
                new BeanPropertyRowMapper<>(Tag.class), id);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(tags));
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_ONE_SQL, id) > 0;
    }
}
