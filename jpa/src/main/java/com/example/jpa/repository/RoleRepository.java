package com.example.jpa.repository;

import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {

    Optional<IcesiRole> findByName(String role);
}
