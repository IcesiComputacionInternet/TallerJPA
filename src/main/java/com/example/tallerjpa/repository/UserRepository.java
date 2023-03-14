package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository <IcesiUser, UUID> {
}
