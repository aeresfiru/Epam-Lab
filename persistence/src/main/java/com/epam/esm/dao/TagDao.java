package com.epam.esm.dao;

import com.epam.esm.domain.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("tagDao")
public interface TagDao extends BaseDao<Tag, Long> {

}
