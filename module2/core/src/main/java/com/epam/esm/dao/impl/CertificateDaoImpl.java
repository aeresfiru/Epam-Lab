package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private static final String CREATE_ONE_SQL = """
            INSERT INTO gifts.certificates(name, description, price, duration)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SELECT_ALL_SQL = """
            SELECT c.id, c.name, c.description, c.price, c.duration, c.created, c.updated
            FROM gifts.certificates c;
            """;

    private static final String SELECT_ONE_SQL = """
            SELECT c.id, c.name, c.description, c.price, c.duration,
            c.created, c.updated FROM gifts.certificates c WHERE c.id = ?
            """;

    private static final String UPDATE_ONE_SQL = """
            UPDATE gifts.certificates c SET name = ?, description = ?, price = ?, duration = ?
            WHERE id = ?
            """;

    private static final String DELETE_ONE_SQL =
            "DELETE FROM gifts.certificates WHERE certificates.id = ?";

    private static final String ATTACH_TAG_SQL =
            "INSERT INTO gifts.certificate_tags(certificate_id, tag_id) VALUES (?, ?);";

    private static final String DETACH_TAG_SQL =
            "DELETE FROM gifts.certificate_tags WHERE certificate_id = ? AND tag_id = ?;";

    private static final String SELECT_ONE_BY_NAME_SQL = """
            SELECT c.id, c.name, c.description, c.price, c.duration,
            c.created, c.updated FROM gifts.certificates c WHERE c.name = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Certificate> certificateRowMapper;

    @Autowired
    public CertificateDaoImpl(JdbcTemplate jdbcTemplate,
                              RowMapper<Certificate> certificateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.certificateRowMapper = certificateRowMapper;
    }

    @Override
    public Certificate create(Certificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> getPreparedStatement(certificate, con, CREATE_ONE_SQL), keyHolder);
        certificate.setId((Long) keyHolder.getKey());
        return certificate;
    }

    private static PreparedStatement getPreparedStatement(Certificate certificate,
                                                          Connection con,
                                                          String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});

        ps.setString(1, certificate.getName());
        ps.setString(2, certificate.getDescription());
        ps.setBigDecimal(3, certificate.getPrice());
        ps.setShort(4, certificate.getDuration());
        return ps;
    }

    @Override
    public List<Certificate> readAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, certificateRowMapper);
    }

    @Override
    public Optional<Certificate> readById(Long id) {
        List<Certificate> certificates =
                jdbcTemplate.query(SELECT_ONE_SQL, certificateRowMapper, id);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(certificates));
    }

    @Override
    public Certificate update(Certificate certificate) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = getPreparedStatement(certificate, con, UPDATE_ONE_SQL);
            ps.setLong(5, certificate.getId());
            return ps;
        });
        return certificate;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_ONE_SQL, id);
    }

    @Override
    public boolean attachTagToCertificate(long certificateId, long tagId) {
        return jdbcTemplate.update(ATTACH_TAG_SQL, certificateId, tagId) == 1;
    }

    @Override
    public boolean detachTagFromCertificate(long certificateId, long tagId) {
        return jdbcTemplate.update(DETACH_TAG_SQL, certificateId, tagId) == 1;
    }

    /*@Override
    public List<Certificate> query(CertificateSelectQueryConfig config) {
        PreparedStatementCreator creator = new CertificateSelectQueryCreator(config);
        return jdbcTemplate.query(creator, certificateRowMapper);
    }*/

    @Override
    public Optional<Certificate> readByName(String name) {
        List<Certificate> certificates =
                jdbcTemplate.query(SELECT_ONE_BY_NAME_SQL, certificateRowMapper, name);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(certificates));
    }
}
