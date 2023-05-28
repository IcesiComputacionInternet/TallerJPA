package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository <IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber =:accountNumber")
    boolean existsByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT a.active FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    boolean isActive(@Param("accountNumber") String accountNumber);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Optional<IcesiAccount> getAccount(@Param("accountNumber") String accountNumber);

    @Query("SELECT a FROM IcesiAccount a WHERE a.icesiUser.userId = :userId")
    List<IcesiAccount> getAllAccounts(@Param("userId") UUID userID);


}
