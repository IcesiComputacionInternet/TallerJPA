package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(String number);


}
