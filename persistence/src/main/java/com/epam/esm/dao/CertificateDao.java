package com.epam.esm.dao;

import com.epam.esm.dao.builder.QueryCertificateConfig;
import com.epam.esm.domain.Certificate;

import java.util.List;

public interface CertificateDao extends BaseDao<Certificate, Long> {

    boolean attachTagToCertificate(long certificateId, long tagId);

    boolean detachTagFromCertificate(long certificateId, long tagId);

    List<Certificate> query(QueryCertificateConfig config);
}
