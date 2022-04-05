package com.epam.esm.dao;

import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@Qualifier("tagDao")
public interface TagDao extends BaseDao<Tag, Long> {

    Set<Tag> readByCertificateId(long certificateId);

    Optional<Tag> readByName(String name);
}
