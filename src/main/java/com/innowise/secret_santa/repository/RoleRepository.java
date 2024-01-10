package com.innowise.secret_santa.repository;

import com.innowise.secret_santa.model.postgres.Role;
import com.innowise.secret_santa.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName (RoleEnum role);
}
