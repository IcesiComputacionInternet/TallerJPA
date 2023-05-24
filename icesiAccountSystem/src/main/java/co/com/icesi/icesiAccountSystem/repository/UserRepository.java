package co.com.icesi.icesiAccountSystem.repository;

import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {
    @Query("SELECT user FROM IcesiUser user WHERE  user.email= :email")
    Optional<IcesiUser> findByEmail(@Param("email") String email);
    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    Optional<IcesiUser> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
