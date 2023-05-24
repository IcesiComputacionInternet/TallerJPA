package co.com.icesi.icesiAccountSystem.repository;

import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(@Param("accountNumber") String accountNumber);

}
