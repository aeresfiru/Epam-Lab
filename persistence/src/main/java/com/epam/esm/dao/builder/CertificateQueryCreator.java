package com.epam.esm.dao.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CertificateQueryCreator extends QueryCreator {

    private static final String SELECT_QUERY =
            "SELECT c.id, c.name, c.description, c.price, c.duration," +
            "c.create_date, c.last_update_date FROM gift_certificate c";

    private static final String TAG_JOIN_QUERY =
            "JOIN gift_certificate_tag gct on c.id = gct.certificate_id\n" +
            "JOIN tag t on t.id = gct.tag_id";

    private static final String TAG_NAME_QUERY =
            "t.name LIKE CONCAT('%', ? ,' %')";

    private static final String CERTIFICATE_NAME_QUERY =
            "c.name LIKE CONCAT('%', ? ,' %')";

    private static final String CERTIFICATE_DESCRIPTION_QUERY =
            "c.description LIKE CONCAT('%', ? ,' %')";

    private final QueryCertificateConfig config;

    public CertificateQueryCreator(QueryCertificateConfig config) {
        this.config = config;
    }

    private StringBuilder builder;
    private boolean hasWhereStatement = false;

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        this.builder = new StringBuilder(SELECT_QUERY);
        PreparedStatement st = con.prepareStatement(this.buildQuery());
        this.setParameters(st);
        return st;
    }

    @Override
    protected String buildQuery() {
        if (config.getTagParam() != null) {
            this.attachTagQueryPart();
            hasWhereStatement = true;
        }
        if (config.getSearchQuery() != null) {
            this.attachSearchQueryPart();
            hasWhereStatement = true;
        }
        if (config.getParameterSortingTypeMap() != null) {
            this.attachSortingType();
        }
        return builder.toString();
    }

    public void attachSearchQueryPart() {
        builder.append(WHITESPACE)
                .append(defineWhereOrAnd())
                .append(WHITESPACE)
                .append(CERTIFICATE_NAME_QUERY)
                .append(WHITESPACE)
                .append(OR)
                .append(WHITESPACE)
                .append(CERTIFICATE_DESCRIPTION_QUERY);
    }

    private void attachTagQueryPart() {
        builder.append(WHITESPACE)
                .append(TAG_JOIN_QUERY)
                .append(WHITESPACE)
                .append(WHERE)
                .append(WHITESPACE)
                .append(TAG_NAME_QUERY);
    }

    private void attachSortingType() {
        builder.append(ORDER_BY);
        this.attachSortingParameters();
    }

    private void attachSortingParameters() {
        config.getParameterSortingTypeMap()
                .forEach(this::attachSortingParameter);
        builder.deleteCharAt(builder.lastIndexOf(","));
    }

    private void attachSortingParameter(String key, SortingType value) {
        builder.append(WHITESPACE)
                .append(key)
                .append(WHITESPACE)
                .append(value.name())
                .append(",");
    }

    protected void setParameters(PreparedStatement st) throws SQLException {
        int index = 1;
        if (config.getTagParam() != null) {
            st.setObject(index++, config.getTagParam());
        }
        if (config.getSearchQuery() != null) {
            st.setObject(index++, config.getSearchQuery());
            st.setObject(index, config.getSearchQuery());
        }
    }

    private String defineWhereOrAnd() {
        if (hasWhereStatement) {
            return AND;
        } else {
            return WHERE;
        }
    }

}
