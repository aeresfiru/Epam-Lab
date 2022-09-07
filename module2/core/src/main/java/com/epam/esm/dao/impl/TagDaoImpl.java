package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String CREATE_ONE_SQL =
            "INSERT INTO gifts.tags(name) VALUES (?);";

    private static final String SELECT_ALL_SQL =
            "SELECT tags.id, tags.name FROM gifts.tags;";

    private static final String SELECT_ONE_SQL =
            "SELECT tags.id, tags.name FROM gifts.tags WHERE tags.id = ?;";

    private static final String DELETE_ONE_SQL =
            "DELETE FROM gifts.tags WHERE gifts.tags.id = ?;";

    private static final String SELECT_BY_CERTIFICATE_ID_SQL = """
            SELECT tags.id, tags.name FROM gifts.tags
            JOIN gifts.certificate_tags gct on tags.id = gct.tag_id WHERE gct.certificate_id = ?;
            """;

    private static final String SELECT_BY_NAME_SQL =
            "SELECT tags.id, tags.name FROM gifts.tags WHERE tags.name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate,
                      RowMapper<Tag> tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Tag create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE_ONE_SQL, new String[]{"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        tag.setId((Long) keyHolder.getKey());
        return tag;
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, tagRowMapper);
    }

    @Override
    public Optional<Tag> readById(Long id) {
        List<Tag> tags = jdbcTemplate.query(SELECT_ONE_SQL, tagRowMapper, id);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(tags));
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException("Update tag is not supported.");
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_ONE_SQL, id);
    }

    @Override
    public Set<Tag> readByCertificateId(long certificateId) {
        return new HashSet<>(jdbcTemplate.query(SELECT_BY_CERTIFICATE_ID_SQL, tagRowMapper, certificateId));
    }

    @Override
    public Optional<Tag> readByName(String name) {
        List<Tag> tags = jdbcTemplate.query(SELECT_BY_NAME_SQL, tagRowMapper, name);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(tags));
    }
}
