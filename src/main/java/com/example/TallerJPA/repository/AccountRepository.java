package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("select a from IcesiAccount a where a.accountNumber = ?1")
    public Optional<IcesiAccount> findByAccountNumber(String accountNumber);
}
