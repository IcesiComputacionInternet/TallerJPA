package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {
    @Query("select r from IcesiRole r where r.name = ?1")
    Optional<IcesiRole> findByName(String name);

}
