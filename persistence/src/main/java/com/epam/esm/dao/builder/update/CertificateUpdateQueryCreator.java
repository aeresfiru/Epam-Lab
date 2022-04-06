package com.epam.esm.dao.builder.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CertificateUpdateQueryCreator extends UpdateQueryCreator {

    private static final String TABLE_NAME = "gift_certificate";
    private static final String ID_EQ_ID = "id = ?";

    //tags and id need to be removed because we don't include it in SET statement
    private static final String TAGS = "tags";
    private static final String ID = "id";

    private final CertificateUpdateQueryConfig config;

    public CertificateUpdateQueryCreator(CertificateUpdateQueryConfig config) {
        this.config = config;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        String query = this.buildQuery();
        PreparedStatement st = con.prepareStatement(query);
        st.setObject(1, config.getCertificateId());
        return st;
    }

    protected String buildQuery() {
        this.removeUnnecessaryFieldsForUpdate();
        this.update(TABLE_NAME);
        this.set(config.getParamsValueMap());
        this.where(ID_EQ_ID);
        return builder.toString();
    }

    private void removeUnnecessaryFieldsForUpdate() {
        config.getParamsValueMap().remove(TAGS);
        config.getParamsValueMap().remove(ID);
    }
}
