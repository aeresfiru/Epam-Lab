package com.epam.esm.repository.specification;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CertificateSpecifications
 *
 * @author alex
 * @version 1.0
 * @since 8.05.22
 */
public class CertificateSpecification {

    private CertificateSpecification() {
    }

    public static Specification<Certificate> certificateHasTags(Collection<String> tagNames) {
        return (root, query, cb) -> {
            if (tagNames == null) {
                return cb.conjunction();
            }
            List<Predicate> restrictions = new ArrayList<>();
            for (String tagName : tagNames) {
                restrictions.add(cb.equal(root.join("tags").get("name"), tagName));
            }
            return cb.and(restrictions.toArray(new Predicate[0]));
        };
    }

    public static Specification<Certificate> certificateNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null) {
                return cb.conjunction();
            }
            return cb.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Certificate> certificateDescriptionLike(String description) {
        return (root, query, cb) -> {
            if (description == null) {
                return cb.conjunction();
            }
            return cb.like(root.get("description"), "%" + description + "%");
        };
    }

    public static Specification<Certificate> certificateStatusIs(Status status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }
}
