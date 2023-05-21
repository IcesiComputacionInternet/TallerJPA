package icesi.university.accountSystem.repository;

import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiAccount u WHERE u.accountNumber = :accountNumber")
    boolean existsByAccountNumber(String accountNumber);
    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber AND a.active = :isActive")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber, boolean isActive);
    @Modifying
    @Query("UPDATE IcesiAccount a SET a.balance = :balance WHERE a.accountNumber = :accountNumber")
    void updateBalance(Long balance, String accountNumber);
    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = CASE WHEN a.balance > 0 THEN true END WHERE (a.accountNumber = :accountNumber)")
    void activateAccount(String accountNumber);
    @Modifying
    @Query("UPDATE IcesiAccount a SET a.active = CASE WHEN a.balance = 0 THEN false END WHERE (a.accountNumber = :accountNumber)")
    void deactivateAccount(String accountNumber);
    @Query("SELECT a.active FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    boolean isActive(String accountNumber);
}
