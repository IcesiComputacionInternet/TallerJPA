package icesi.university.accountSystem.repository;

import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT accountNumber FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);
}
