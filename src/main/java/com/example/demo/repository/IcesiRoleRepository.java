package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.IcesiRole;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {
    
    Optional<IcesiRole> findByName(String name);
}
