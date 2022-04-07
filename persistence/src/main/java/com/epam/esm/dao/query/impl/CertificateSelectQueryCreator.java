package com.epam.esm.dao.query.impl;

import com.epam.esm.dao.query.SelectQueryCreator;
import com.epam.esm.dao.query.WhereInfo;
import com.epam.esm.dao.query.config.CertificateSelectQueryConfig;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * CertificateQueryCreator
 *
 * @author alex
 * @version 1.0
 * @since 8.04.22
 */
public class CertificateSelectQueryCreator implements PreparedStatementCreator {
    private static final String CERTIFICATE_TABLE_NAME = "gift_certificate c";
    private static final String CERTIFICATE_ID = "c.id";
    private static final String CERTIFICATE_NAME = "c.name";
    private static final String CERTIFICATE_DESCRIPTION = "c.description";
    private static final String CERTIFICATE_PRICE = "c.price";
    private static final String CERTIFICATE_DURATION = "c.duration";
    private static final String CERTIFICATE_CREATE_DATE = "c.create_date";
    private static final String CERTIFICATE_LAST_UPDATE_DATE = "c.last_update_date";

    private static final String TAG_TABLE_NAME = "tag t";
    private static final String TAG_ID = "t.id";
    private static final String TAG_NAME = "t.name";

    private static final String CERTIFICATE_TAG_TABLE_NAME = "gift_certificate_tag gfc";
    private static final String CERTIFICATE_TAG_CERTIFICATE_ID = "gfc.certificate_id";
    private static final String CERTIFICATE_TAG_TAG_ID = "gfc.tag_id";

    private final SelectQueryCreator selectQueryCreator;

    private final CertificateSelectQueryConfig config;

    public CertificateSelectQueryCreator(CertificateSelectQueryConfig config) {
        this.selectQueryCreator = new SelectQueryCreator();
        this.config = config;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        selectQueryCreator.tableName(CERTIFICATE_TABLE_NAME);
        selectQueryCreator.tableFields(CERTIFICATE_ID, CERTIFICATE_NAME, CERTIFICATE_DESCRIPTION,
                CERTIFICATE_PRICE, CERTIFICATE_DURATION, CERTIFICATE_CREATE_DATE, CERTIFICATE_LAST_UPDATE_DATE);
        selectQueryCreator.distinct();

        if (config.getSearchQuery() != null) {
            String searchQuery = "%" + config.getSearchQuery() + "%";
            WhereInfo.WhereOperator operator = WhereInfo.WhereOperator.LIKE;
            selectQueryCreator.addWhere(CERTIFICATE_NAME, searchQuery, operator,
                    true, false, false);
            selectQueryCreator.addWhere(CERTIFICATE_DESCRIPTION, searchQuery, operator,
                    false, true, true);
        }

        if (config.getTagParam() != null) {
            String searchTag = "%" + config.getTagParam() + "%";
            selectQueryCreator.join(CERTIFICATE_TAG_TABLE_NAME, CERTIFICATE_ID, CERTIFICATE_TAG_CERTIFICATE_ID);
            selectQueryCreator.join(TAG_TABLE_NAME, CERTIFICATE_TAG_TAG_ID, TAG_ID);
            selectQueryCreator.addWhere(TAG_NAME, searchTag, WhereInfo.WhereOperator.LIKE,
                    false, false, false);
        }

        if (config.getParameterSortingTypeMap() != null) {
            selectQueryCreator.orderBy(config.getParameterSortingTypeMap());
        }
        return con.prepareStatement(selectQueryCreator.createSqlQuery());
    }
}
