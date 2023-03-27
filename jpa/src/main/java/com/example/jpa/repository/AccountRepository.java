package com.example.jpa.repository;

import com.example.jpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiAccount u WHERE u.accountNumber = :accountNumber")
    boolean findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Optional<IcesiAccount> getByAccountNumber(String accountNumber);

    @Query(value = "SELECT a FROM IcesiAccount  a where a.active = true")
    List<IcesiAccount> getAllAccounts();

}
