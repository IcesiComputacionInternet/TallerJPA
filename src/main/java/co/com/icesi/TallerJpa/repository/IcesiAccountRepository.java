package co.com.icesi.TallerJpa.repository;

import co.com.icesi.TallerJpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiAccountRepository extends JpaRepository<IcesiAccount, UUID> {

    /**
     * Get icesi account by its account number.
     * @param accountNumber is the unique account number of the Icesi Account.
     * @return optional of icesi account.
     */
    @Query( "SELECT ia " +
            "FROM IcesiAccount ia " +
            "WHERE ia.accountNumber = :accountNumber")
    Optional<IcesiAccount> findByAccountNumber(@Param("accountNumber") String accountNumber);

    /**
     * Get all te icesi accounts by the user id of the Icesi User owner.
     * @param userId is the unique user Id of the Icesi User.
     * @return list of icesi account.
     */
    @Query( "SELECT ia " +
            "FROM IcesiAccount ia " +
            "WHERE ia.icesiUser.userId = :userId")
    List<IcesiAccount> findAllByIcesiUser(@Param("userId") UUID userId);

    @Query( "SELECT ia " +
            "FROM IcesiAccount ia " +
            "JOIN IcesiUser iu " +
            "ON ia.icesiUser.userId = iu.userId " +
            "WHERE iu.email = :email")
    List<IcesiAccount> findAllByEmail(@Param("email")String email);

    @Modifying
    @Query( "UPDATE IcesiAccount ia " +
            "SET ia.active = true " +
            "WHERE ia.accountNumber = :accountNumber")
    void enableByAccountNumber(@Param("accountNumber") String accountNumber);

    @Modifying
    @Query( "UPDATE IcesiAccount ia " +
            "SET ia.active = CASE WHEN ia.balance = 0 THEN false END " +
            "WHERE ia.accountNumber = :accountNumber")
    void disableByAccountNumber(@Param("accountNumber") String accountNumber);

    @Modifying
    @Query( "UPDATE IcesiAccount ia " +
            "SET ia.balance = :newBalance " +
            "WHERE ia.accountNumber = :accountNumber")
    void updateBalance(@Param("newBalance") Long balance, @Param("accountNumber") String accountNumber);

    @Query( "SELECT ia.active " +
            "FROM IcesiAccount ia " +
            "WHERE ia.accountNumber = :accountNumber")
    boolean isActiveByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query( "SELECT CASE WHEN (COUNT(ia) > 0) " +
            "THEN true " +
            "ELSE false END " +
            "FROM IcesiAccount ia " +
            "WHERE ia.accountNumber = :accountNumber")
    boolean existsByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query( "SELECT CASE WHEN (COUNT(ia) > 0) " +
            "THEN true " +
            "ELSE false END " +
            "FROM IcesiAccount ia " +
            "WHERE ia.accountNumber = :accountNumber " +
            "AND ia.icesiUser = :userId")
    boolean isIcesiAccountOwner(@Param("userId") UUID userId, @Param("accountNumber") String accountNumber);
}
