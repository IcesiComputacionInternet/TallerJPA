package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT CASE WHEN (COUNT(user) > 0) THEN true ELSE false END FROM IcesiUser user WHERE user.email = :email")
    boolean isByEmail(String email);

    @Query("SELECT CASE WHEN (COUNT(user) > 0) THEN true ELSE false END FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    boolean findByPhone(String phoneNumber);

    @Query("SELECT u FROM IcesiUser u WHERE u.email = :email")
    static Optional<IcesiUser> findByEmail(String email);
}
