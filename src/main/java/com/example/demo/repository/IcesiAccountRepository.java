package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.IcesiAccount;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);
    Optional<IcesiAccount> findByBalance(long balance);
}
