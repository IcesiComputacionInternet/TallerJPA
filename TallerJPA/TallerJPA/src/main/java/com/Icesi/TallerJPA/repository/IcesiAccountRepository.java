package com.Icesi.TallerJPA.repository;

import com.Icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    IcesiAccount returnAccount(String accountNumber);

    @Query("UPDATE IcesiAccount account SET account.active = CASE WHEN balance > 0 THEN true ELSE false END WHERE accountNumber = :accountNumber")
    void enableAccount(String accountNumber);

    @Modifying
    @Query(value = "UPDATE IcesiAccount account SET account.active = false WHERE account.accountNumber = :accountNumber")
    void disableAccount(@Param("accountNumber") String accountNumber);



}