package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.builder.QueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TagRepositoryImpl
 *
 * @author alex
 * @version 1.0
 * @since 17.04.22
 */
@Repository
public class TagRepositoryImpl extends AbstractRepository<Tag>
        implements TagRepository {

    private static final String FIND_MOST_WIDELY_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS_QUERY =
            "SELECT tags.id, tags.name\n"
                    + "FROM orders\n"
                    + "         LEFT JOIN user_order_gift_certificate uogc ON uogc.user_order_id = orders.id\n"
                    + "         LEFT JOIN certificates gc on uogc.gift_certificate_id = gc.id\n"
                    + "         INNER JOIN gift_certificate_tag gct on gc.id = gct.gift_certificate_id\n"
                    + "         LEFT JOIN tags ON gct.tag_id = tags.id\n"
                    + "WHERE orders.user_id = (SELECT orders.user_id\n"
                    + "                            FROM orders\n"
                    + "                            GROUP BY orders.user_id\n"
                    + "                            ORDER BY SUM(orders.cost) DESC\n"
                    + "                            LIMIT 1)\n"
                    + "GROUP BY tags.id, tags.name, orders.user_id\n"
                    + "LIMIT 1";

    private static final String SELECT_BY_NAME =
            "SELECT t FROM Tag t where t.name IN :name";

    protected TagRepositoryImpl() {
        super(Tag.class);
    }

    @Override
    protected QueryBuilder<Tag> getQueryBuilder() {
        throw new UnsupportedOperationException("Operation unsupported");
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        @SuppressWarnings("unchecked")
        List<Tag> tags = em.createQuery(SELECT_BY_NAME)
                .setParameter("name", tagName).getResultList();
        return tags.stream().findAny();
    }

    @Override
    public boolean isExists(long id) {
        return em.find(Tag.class, id) != null;
    }

    @Override
    public Optional<Tag> findTheMostPopularTagOfAUserWithAHighestPrice() {
        @SuppressWarnings("unchecked")
        List<Tag> tags = em.createNativeQuery(FIND_MOST_WIDELY_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS_QUERY, Tag.class)
                .getResultList();
        return tags.stream()
                .findFirst();
    }
}
