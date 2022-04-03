package com.epam.esm.dao;

import com.epam.esm.dao.builder.CertificateQueryConfig;
import com.epam.esm.domain.Certificate;

import java.util.List;

public interface CertificateDao extends BaseDao<Certificate, Long> {

    //TODO: Write test
    boolean attachTagToCertificate(long certificateId, long tagId);

    //TODO: Write test
    boolean detachTagFromCertificate(long certificateId, long tagId);

    List<Certificate> query(CertificateQueryConfig config);
}
