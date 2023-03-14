package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository <IcesiRole, UUID> {
}
