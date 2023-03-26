package com.example.jpa.repository;

import com.example.jpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiRole u WHERE u.name = :name")
    boolean findByName(String name);

    @Query("SELECT r FROM IcesiRole r WHERE r.name = :name")
    Optional<IcesiRole> getByName(String name);

    @Query("SELECT r FROM IcesiRole r WHERE r.roleId = :roleId")
    Optional<IcesiRole> findById(String roleId);
}
