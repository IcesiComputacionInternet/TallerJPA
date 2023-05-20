package co.com.icesi.tallerjpa.repository;

import co.com.icesi.tallerjpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {
    Optional<IcesiUser> findByEmail(String email);
    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    Optional<IcesiUser> findByPhone(String phoneNumber);
}
