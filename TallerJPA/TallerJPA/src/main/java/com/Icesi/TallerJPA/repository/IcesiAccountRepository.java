package com.Icesi.TallerJPA.repository;

import com.Icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    IcesiAccount returnAccount(String accountNumber);

    @Query("UPDATE IcesiAccount account SET account.active = CASE WHEN balance > 0 THEN true ELSE false END WHERE accountNumber = :accountNumber")
    void enableAccount(String accountNumber);

    @Query("UPDATE IcesiAccount account SET account.active = CASE WHEN balance = 0 THEN false ELSE active END WHERE accountNumber = :accountNumber")
    void disableAccount(String accountNumber);
}
