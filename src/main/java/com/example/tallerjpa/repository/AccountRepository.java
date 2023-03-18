package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository <IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber =:accountNumber")
    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a.active FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    boolean isActive(String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = true WHERE (a.accountNumber = :accountNumber)")
    void activateAccount(String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = CASE WHEN a.balance = 0 THEN false END WHERE (a.accountNumber = :accountNumber)")
    void deactivateAccount(String accountNumber);

    @Query("SELECT a.balance FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Long getBalance(String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.balance = a.balance - :amount WHERE a.accountNumber = :accountNumber")
    void withdrawMoney(String accountNumber, Long amount);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    void depositMoney(String accountNumber, Long amount);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber AND a.type <> '0' AND a.active = true")
    Optional<IcesiAccount> getStateOfAccount(String accountNumber);

    @Query("SELECT a.type FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    String getType(String accountNumber);

}
