package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber= :accountNumber AND account.active= :isActive")
    Optional<IcesiAccount> findAccountByAccountNumber(String accountNumber, boolean isActive);
}