package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {
}
