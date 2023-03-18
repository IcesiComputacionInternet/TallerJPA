package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM IcesiAccount r WHERE r.accountNumber = ?1")
    boolean existsByAccountNumber(String accountNumber);

    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

    void saveIcesiAccount(IcesiAccount account);
}
