package com.epam.esm.dao.extractor;

import com.epam.esm.domain.Certificate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateExtractor implements ResultSetExtractor<Certificate> {

    @Override
    public Certificate extractData(ResultSet rs) throws SQLException, DataAccessException {
        return Certificate.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getBigDecimal("price"))
                .duration(rs.getShort("duration"))
                .createDate(rs.getTimestamp("create_date").toLocalDateTime())
                .lastUpdateDate(rs.getTimestamp("last_update_date").toLocalDateTime())
                .build();
    }
}
