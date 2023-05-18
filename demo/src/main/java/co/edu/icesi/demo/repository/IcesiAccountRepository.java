package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Optional<IcesiRole> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :number")
    Optional<IcesiAccount> findByNumber(String number);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Account u WHERE u.accountNumber = :accountNumber")
    boolean existsByAccountNumber(String accountNumber);
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber AND a.active = :isActive")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber, boolean isActive);
    @Modifying
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.accountNumber = :accountNumber")
    void updateBalance(Long balance, String accountNumber);
    @Modifying
    @Query("UPDATE Account a SET a.active = CASE WHEN a.balance > 0 THEN true END WHERE (a.accountNumber = :accountNumber)")
    void enableAccount(String accountNumber);
    @Modifying
    @Query("UPDATE Account a SET a.active = CASE WHEN a.balance = 0 THEN false END WHERE (a.accountNumber = :accountNumber)")
    void disableAccount(String accountNumber);
    @Query("SELECT a.active FROM Account a WHERE a.accountNumber = :accountNumber")
    boolean isActive(String accountNumber);
}
