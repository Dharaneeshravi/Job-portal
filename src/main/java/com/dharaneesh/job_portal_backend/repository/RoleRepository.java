package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Cacheable("roles")
    Optional<Role> findRoleByName(String name);
}
