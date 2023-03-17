package com.example.jpa.repository;

import com.example.jpa.model.IcesiRole;

import java.util.Optional;

public interface RoleRepository {

    Optional<IcesiRole> findByName(String role);
}
