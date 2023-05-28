package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRespository extends JpaRepository<IcesiAccount, UUID> {

    @Query(value = "SELECT a FROM IcesiAccount a where a.accountNumber = :accountNumber and a.active = true ")
    Optional<IcesiAccount> findByAccountNumber(@Param("accountNumber")String accountNumber);

    @Query(value = "SELECT a FROM IcesiAccount a INNER JOIN IcesiUser u ON a.user.userID = u.userID where u.userID = :userId")
    List<IcesiAccount> findAllActivatedByUser(@Param("userId") UUID userId);



}
