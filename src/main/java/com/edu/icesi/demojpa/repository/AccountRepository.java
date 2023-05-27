package com.edu.icesi.demojpa.repository;

import com.edu.icesi.demojpa.model.IcesiAccount;
import com.edu.icesi.demojpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID>{
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber AND account.active = :isActive")
    Optional<IcesiAccount> findAccountByAccountNumber(@Param("accountNumber") String accountNumber, @Param("isActive") boolean isActive);

    @Query("SELECT COUNT(account) > 0 FROM IcesiAccount account WHERE account.accountId = :accountId AND account.accountNumber = :accountNumber")
    Boolean isOwnerAccount(@Param("accountId") UUID accountId, @Param("accountNumber") String accountNumber);

    @Query("SELECT account FROM IcesiAccount account WHERE account.icesiUser.userId = :userId")
    Optional<List<IcesiAccount>> findUserAccount(@Param("userId") UUID userId);
}
