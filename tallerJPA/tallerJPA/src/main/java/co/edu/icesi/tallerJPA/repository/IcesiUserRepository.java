package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
    
    Optional<IcesiUser> findByEmail(String email);

    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);
}
