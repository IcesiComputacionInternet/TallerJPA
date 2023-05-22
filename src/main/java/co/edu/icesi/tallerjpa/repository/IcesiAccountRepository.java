package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM IcesiAccount r WHERE r.accountNumber = ?1")
    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = ?1")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

    @Query("UPDATE IcesiAccount a SET a.balance = ?2 WHERE a.accountNumber = ?1")
    void updateAccountBalance(String accountNumber, Long balance);

    @Query("DELETE FROM IcesiAccount a WHERE a.accountNumber = ?1")
    void deleteByAccountNumber(String accountNumber);
}


