package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("select a from IcesiAccount a where a.accountNumber = ?1")
    public Optional<IcesiAccount> findByAccountNumber(String accountNumber);
    @Modifying
    @Query("update IcesiAccount a set a.balance = ?1 where a.accountNumber = ?2")
    public void updateBalance(Double balance, String accountNumber);
    @Modifying
    @Query("update IcesiAccount a set a.active = ?1 where a.accountNumber = ?2")
    public void changeStatus(Boolean active, String accountNumber);
    @Query("select a.active from IcesiAccount a where a.accountNumber = ?1")
    public boolean isAccountActive(String accountNumber);
}
