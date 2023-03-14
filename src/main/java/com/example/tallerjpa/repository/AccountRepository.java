package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository <IcesiAccount, UUID> {
}
