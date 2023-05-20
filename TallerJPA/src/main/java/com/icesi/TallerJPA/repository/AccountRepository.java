package com.icesi.TallerJPA.repository;

import com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber =:accountNumber")
    Boolean existsByAccountNumber (@Param("accountNumber") String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = true WHERE a.accountNumber = :accountNumber")
    void activeAccount(@Param("accountNumber") String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = false WHERE a.accountNumber = :accountNumber")
    void inactiveAccount(@Param("accountNumber") String accountNumber);

    @Query("SELECT a.active FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Boolean IcesiAccountByActive(@Param("accountNumber") String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.balance = a.balance - :withdrawal WHERE a.accountNumber = :accountNumber")
    void withdrawalAccount(@Param("accountNumber") String accountNumber, @Param("withdrawal") Long withdrawal);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.balance = a.balance + :deposit WHERE a.accountNumber = :accountNumber")
    void depositAccount(@Param("accountNumber") String accountNumber, @Param("deposit") Long deposit);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber AND a.type <> 0 AND a.active = true")
    Optional<IcesiAccount> getTypeofAccount(@Param("accountNumber") String accountNumber);

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber = :accountNumber AND a.icesiUser.userId = :userId")
    Boolean accountOwner(@Param("accountNumber") String accountNumber, @Param("userId") UUID userId);

}
