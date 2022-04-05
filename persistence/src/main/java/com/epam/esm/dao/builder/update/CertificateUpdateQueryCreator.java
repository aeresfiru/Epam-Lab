package com.epam.esm.dao.builder.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CertificateUpdateQueryCreator extends UpdateQueryCreator {

    private static final String TABLE_NAME = "gift_certificate";
    private static final String ID_EQ_ID = "id = ?";

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
        this.builder = new StringBuilder();
        this.update(TABLE_NAME);
        config.getParamsValueMap().remove("tags");
        this.set(config.getParamsValueMap());
        this.where(ID_EQ_ID);
        return builder.toString();
    }
}
