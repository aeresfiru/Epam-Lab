package com.epam.esm.dao.query.impl;

import com.epam.esm.dao.query.UpdateQueryCreator;
import com.epam.esm.dao.query.WhereInfo;
import com.epam.esm.dao.query.config.CertificateUpdateQueryConfig;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * CertificateUpdateQueryCreator
 *
 * @author alex
 * @version 1.0
 * @since 8.04.22
 */
public class CertificateUpdateQueryCreator implements PreparedStatementCreator {

    private static final String TABLE_NAME = "gift_certificate";
    private static final String ID = "id";
    private static final String TAGS = "tags";

    private final CertificateUpdateQueryConfig config;

    private final UpdateQueryCreator creator;

    public CertificateUpdateQueryCreator(CertificateUpdateQueryConfig config) {
        this.config = config;
        this.creator = new UpdateQueryCreator();
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        creator.tableName(TABLE_NAME);
        config.getParamsValueMap().remove(TAGS);
        config.getParamsValueMap().remove(CertificateColumn.LAST_UPDATE.getNameInRequest());
        config.getParamsValueMap().remove(CertificateColumn.CREATE_DATE.getNameInRequest());
        config.getParamsValueMap().put(CertificateColumn.LAST_UPDATE.getNameInDb(), LocalDateTime.now());
        creator.setParameters(config.getParamsValueMap());
        creator.addWhere(ID, String.valueOf(config.getCertificateId()), WhereInfo.WhereOperator.EQUALS);
        return con.prepareStatement(creator.createSqlQuery());
    }
}
