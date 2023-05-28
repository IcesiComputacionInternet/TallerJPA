package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber= :accountNumber AND account.active= :isActive")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber, boolean isActive);


    @Query("SELECT account FROM IcesiAccount account WHERE account.accountNumber= :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

}
