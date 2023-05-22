package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query(value = "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.email = :email")
    boolean findByEmail(String email);

    @Query(value = "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.phoneNumber = :phoneNumber")
    boolean findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT u FROM IcesiUser u WHERE u.email = :email")
    Optional<IcesiUser> findUserByEmail(String email);

    @Modifying
    @Query(value = "UPDATE IcesiUser u SET u.role = :role WHERE u.email = :email")
    void updateRole(String email, IcesiRole role);

/*
@Modifying
    @Query(value = "UPDATE IcesiAccount a SET a.balance = :balance WHERE a.accountNumber = :accountNumber")
    void updateAccount(String accountNumber,Long balance);
 */
}
