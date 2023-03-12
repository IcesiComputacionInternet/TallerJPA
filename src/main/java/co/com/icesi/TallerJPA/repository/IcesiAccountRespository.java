package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IcesiAccountRespository extends JpaRepository<IcesiAccount, UUID> {

    @Query(value = "SELECT a FROM IcesiAccount a where a.accountNumber = :accountNumber and a.active = true ")
    Optional<IcesiAccount> findByAccountNumber(String accountNumber);

    @Query(value = "SELECT a FROM IcesiAccount a where a.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountId(UUID id);


}
