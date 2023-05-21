package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT ia FROM IcesiAccount ia WHERE ia.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE IcesiAccount ia SET ia.active = true WHERE ia.accountId = :id")
    int enableAccount(String id);

    @Modifying
    @Query("UPDATE IcesiAccount ia SET ia.active = false WHERE ia.accountId = :id")
    int disableAccount(String id);

    @Modifying
    @Query("UPDATE IcesiAccount ia SET ia.balance = :newBalance WHERE ia.accountId = :accountId")
    int updateBalance(long newBalance, String accountId);
}
