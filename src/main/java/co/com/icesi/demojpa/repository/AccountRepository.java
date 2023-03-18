package co.com.icesi.demojpa.repository;

import co.com.icesi.demojpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {


    Optional<IcesiAccount> findByAccountNumber(String fromString);

    @Modifying
    @Query(value = "UPDATE IcesiAccount a set a.active= false where a.accountNumber= :accountNumber")
    void disableAccount(String accountNumber);

    @Modifying
    @Query(value = "UPDATE IcesiAccount a set a.active= true where a.accountNumber= :accountNumber")
    void enableAccount(String accountNumber);

    @Modifying
    @Query(value = "UPDATE IcesiAccount a set a.balance= :balance where a.accountNumber= :accountNumber")
    void updateBalance(String accountNumber, long balance);



}
