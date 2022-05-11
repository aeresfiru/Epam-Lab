package com.epam.esm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Certificate
 *
 * @author alex
 * @version 1.0
 * @since 17.04.22
 */
@Entity
@Table(name = "gift_certificate")
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Certificate extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "duration",nullable = false)
    private Short duration;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id",
                    nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id",
                    nullable = false, updatable = false))
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "certificates")
    @ToString.Exclude
    private Set<Order> orders = new HashSet<>();

    public Certificate(long id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Certificate that = (Certificate) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
