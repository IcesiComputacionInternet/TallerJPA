package co.edu.icesi.tallerjpa.runableartefact.repository;

import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM IcesiUser u WHERE u.email = ?1")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM IcesiUser u WHERE u.phoneNumber = ?1")
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<IcesiUser> findByEmail(String email);

}
