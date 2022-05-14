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

    @Query(nativeQuery = true,
            value = "SELECT tags.id, tags.name\n" +
                    "FROM tags\n" +
                    "         JOIN gift_certificate_tag gct on tags.id = gct.tag_id\n" +
                    "         JOIN user_order_gift_certificate uogc on gct.gift_certificate_id = uogc.gift_certificate_id\n" +
                    "         JOIN orders uo ON uogc.user_order_id = uo.id\n" +
                    "    AND uo.user_id = (SELECT u.id\n" +
                    "                      FROM users u\n" +
                    "                               JOIN orders o on u.id = o.user_id\n" +
                    "                      GROUP BY u.id\n" +
                    "                      ORDER BY SUM(uo.cost) DESC\n" +
                    "                      LIMIT 1)\n" +
                    "GROUP BY tags.id, tags.name\n" +
                    "ORDER BY COUNT(tags.id) DESC\n" +
                    "LIMIT 1;")
    Tag findMostUsedTagOfUserWithHighestCostOfAllOrders();

    boolean existsByName(String name);
}
