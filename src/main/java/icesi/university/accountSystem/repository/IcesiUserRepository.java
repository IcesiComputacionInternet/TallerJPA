package icesi.university.accountSystem.repository;

import icesi.university.accountSystem.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
    @Query("SELECT u FROM IcesiUser u WHERE u.email = :email")
    Optional<IcesiUser> findByEmail(String email);
    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

    @Query("SELECT CASE WHEN (COUNT(user) > 0) THEN true ELSE false END FROM IcesiUser user WHERE user.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN (COUNT(user) > 0) THEN true ELSE false END FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    boolean existsByPhoneNumber(String phoneNumber);
}
