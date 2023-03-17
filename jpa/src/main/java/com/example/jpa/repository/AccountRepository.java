package com.example.jpa.repository;

import com.example.jpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiAccount u WHERE u.accountNumber = :accountNumber")
    boolean findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Optional<IcesiAccount> getByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.balance = :balance WHERE a.accountNumber = :accountNumber")
    void updateBalance(Long balance, String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = CASE WHEN a.balance > 0 THEN true END WHERE (a.accountNumber = :accountNumber)")
    void enableAccount(String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = CASE WHEN a.balance = 0 THEN false END WHERE (a.accountNumber = :accountNumber)")
    void disableAccount(String accountNumber);

    @Query("SELECT a.active FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    boolean isActive(String accountNumber);
}
