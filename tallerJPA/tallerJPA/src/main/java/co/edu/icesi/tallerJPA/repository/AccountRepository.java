package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("SELECT CASE WHEN (COUNT(a) > 0) THEN true ELSE false END FROM Account a WHERE a.accountNumber = :accountNumber")
    boolean isByAccountNumber(String accountNumber);

    @Query("SELECT account FROM Account account WHERE account.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(String accountNumber);
    @Modifying
    @Query("UPDATE Account account SET account.balance = :balance WHERE account.accountNumber = :accountNumber")
    void updateBalance(Long balance, String accountNumber);
    @Modifying
    @Query("UPDATE Account account SET account.active = CASE WHEN account.balance > 0 THEN true ELSE false END WHERE (a.accountNumber = :accountNumber)")
    void enableOrDisableAccount(String accountNumber);
    @Query("SELECT account.active FROM Account account WHERE account.accountNumber = :accountNumber")
    boolean isActive(String accountNumber);

}
