package com.Icesi.TallerJPA.repository;

import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount>findByAccountNumber(String accountNumber);
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    IcesiAccount findByAccount(String accountNumber);
}
