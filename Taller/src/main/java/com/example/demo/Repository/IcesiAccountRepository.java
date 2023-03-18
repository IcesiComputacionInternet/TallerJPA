package com.example.demo.Repository;

import com.example.demo.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    IcesiAccount findByAccountNumber(String accountNumber);
}