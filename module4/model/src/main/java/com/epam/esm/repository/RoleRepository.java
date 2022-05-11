package com.epam.esm.repository;

import com.epam.esm.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RoleRepository
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
