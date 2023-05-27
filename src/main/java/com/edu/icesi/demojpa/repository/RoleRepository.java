package com.edu.icesi.demojpa.repository;

import com.edu.icesi.demojpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {
    @Query("SELECT role FROM IcesiRole role WHERE role.name = :name")
    Optional<IcesiRole> findRoleByName(@Param("name") String name);
}
