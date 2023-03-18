package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository <IcesiRole, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiRole u WHERE u.name = :name")
    boolean existsByName(String name);

    @Query("SELECT role FROM IcesiRole role WHERE role.name = :name")
    Optional<IcesiRole> searchByName(@Param("name") String name);

}
