package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT a FROM IcesiAccount a WHERE a.accountNumber = :accountNumber")
    Optional<IcesiRole> findByAccountNumber(String accountNumber);


}
