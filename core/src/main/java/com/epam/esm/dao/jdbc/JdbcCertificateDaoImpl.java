package com.epam.esm.dao.jdbc;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.domain.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCertificateDaoImpl implements CertificateDao {

    private static final String CREATE_ONE_SQL =
            "INSERT INTO gift_certificate(name, description, price, duration,"
                    + "create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT c.id, c.name, c.description, c.price, c.duration,"
                    + "c.create_date, c.last_update_date FROM gift_certificate AS c;";

    private static final String SELECT_ONE_SQL =
            "SELECT c.id, c.name, c.description, c.price, c.duration,"
                    + "c.create_date, c.last_update_date FROM gift_certificate AS c WHERE c.id = ?";

    private static final String UPDATE_ONE_SQL =
            "UPDATE gift_certificate AS c SET c.id = ?, c.name = ?, c.price = ?,"
                    + "c.duration = ?, c.last_update_date = ? WHERE c.id = ?";

    private static final String DELETE_ONE_SQL =
            "DELETE FROM gift_certificate WHERE gift_certificate.id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    }

    @Override
    public boolean create(Certificate certificate) {
        simpleJdbcInsert.withTableName("gift_certificate").usingGeneratedKeyColumns("id");
        Number id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(certificate));
        certificate.setId(id.longValue());
        return jdbcTemplate.update(CREATE_ONE_SQL, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getCreateDate(), Timestamp.from(Instant.now())) == 1;
    }

    @Override
    public List<Certificate> readAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL,
                new BeanPropertyRowMapper<>(Certificate.class));
    }

    @Override
    public Optional<Certificate> read(long id) {
        List<Certificate> certificates = jdbcTemplate.query(SELECT_ONE_SQL,
                new BeanPropertyRowMapper<>(Certificate.class), id);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(certificates));
    }

    @Override
    public boolean update(Certificate certificate) {
        return jdbcTemplate.update(UPDATE_ONE_SQL, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getCreateDate(), certificate.getId()) == 1;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(DELETE_ONE_SQL, id) > 0;
    }
}
