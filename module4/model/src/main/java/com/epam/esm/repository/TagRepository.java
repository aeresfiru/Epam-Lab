package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * TagRepository
 *
 * @author alex
 * @version 1.0
 * @since 7.05.22
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    @Query(value = """
            SELECT tags.id, tags.name
                FROM tags
                         JOIN gift_certificate_tag gct on tags.id = gct.tag_id
                         JOIN user_order_gift_certificate uogc on gct.gift_certificate_id = uogc.gift_certificate_id
                         JOIN orders uo ON uogc.user_order_id = uo.id
                    AND uo.user_id = (SELECT u.id
                                      FROM users u
                                               JOIN orders o on u.id = o.user_id
                                      GROUP BY u.id
                                      ORDER BY SUM(uo.cost) DESC
                                      LIMIT 1)
                GROUP BY tags.id, tags.name
                ORDER BY COUNT(tags.id) DESC
                LIMIT 1""", nativeQuery = true)
    Tag findMostUsedTagOfUserWithHighestCostOfAllOrders();
}
