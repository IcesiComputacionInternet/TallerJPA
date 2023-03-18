package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT icesiUser  FROM IcesiUser icesiUser WHERE  icesiUser.email= :email")
    Optional<IcesiUser> findByEmail(String email);
    @Query("SELECT icesiUser FROM IcesiUser icesiUser WHERE  icesiUser.phoneNumber= :phoneNumber")
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);
}
