package com.edu.icesi.TallerJPA.repository;

import com.edu.icesi.TallerJPA.dto.IcesiAccountDTO;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount>findByAccountNumber(String accountNumber);

    @Query("SELECT account FROM IcesiAccount account WHERE account.icesiUser.userId = :icesi_user")
    List<IcesiAccount> findByAccounts(UUID icesi_user);
    @Query("UPDATE IcesiAccount account SET account.active = CASE WHEN balance > 0 THEN true ELSE false END WHERE accountNumber = :accountNumber")
    void enableAccount(String accountNumber);

    @Modifying
    @Query(value = "UPDATE IcesiAccount account SET account.active = false WHERE account.accountNumber = :accountNumber")
    void disableAccount(@Param("accountNumber") String accountNumber);

}
