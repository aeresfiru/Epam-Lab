package com.epam.esm.dao;

import com.epam.esm.dao.builder.select.CertificateSelectQueryConfig;
import com.epam.esm.dao.builder.update.CertificateUpdateQueryConfig;
import com.epam.esm.domain.Certificate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("certificateDao")
public interface CertificateDao extends BaseDao<Certificate, Long> {

    boolean update(CertificateUpdateQueryConfig config);

    boolean attachTagToCertificate(long certificateId, long tagId);

    boolean detachTagFromCertificate(long certificateId, long tagId);

    List<Certificate> query(CertificateSelectQueryConfig config);
}
