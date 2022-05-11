package com.epam.esm.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Role
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    @Override
    public String toString() {
        return "Role{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
