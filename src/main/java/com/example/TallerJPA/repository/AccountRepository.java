package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

}
