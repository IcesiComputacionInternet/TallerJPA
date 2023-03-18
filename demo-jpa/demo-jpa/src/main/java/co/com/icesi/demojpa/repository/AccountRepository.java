package co.com.icesi.demojpa.repository;

import co.com.icesi.demojpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    Optional<IcesiAccount> findByAccountNumber(String number);

    @Query(value = "UPDATE IcesiAccount c SET c.isActive = FALSE WHERE c.accountNumber = :number")
    Optional<IcesiAccount> disable(String number);

    @Query(value = "UPDATE IcesiAccount c SET c.isActive = TRUE WHERE c.accountNumber = :number")
    Optional<IcesiAccount> enable(String number);

    @Query(value = "UPDATE IcesiAccount c SET c.balance = :balance WHERE c.accountNumber = :number")
    Optional<IcesiAccount> updateBalance(String number, long balance);
}
