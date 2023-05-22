package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {
    @Query(value = "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    boolean findByAccountNumber(String accountNumber);

    @Query(value = "SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Optional<IcesiAccount> findAccount(String accountNumber);

    @Modifying
    @Query(value = "UPDATE IcesiAccount a SET a.balance = :balance WHERE a.accountNumber = :accountNumber")
    void updateAccount(String accountNumber,Long balance);

    @Modifying
    @Query(value = "UPDATE IcesiAccount a SET a.active = :state WHERE a.accountNumber = :accountNumber")
    void updateState(String accountNumber,boolean state);

    @Query(value =  "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber = :accountNumber AND a.user = :user")
    boolean findIfUserIsOwner(String accountNumber, IcesiUser user);
}
