package com.example.demo.Repository;

import com.example.demo.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    IcesiAccount findByAccountNumber(String accountNumber);
}