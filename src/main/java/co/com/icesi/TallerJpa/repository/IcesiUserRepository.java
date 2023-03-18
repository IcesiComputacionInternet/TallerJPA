package co.com.icesi.TallerJpa.repository;

import co.com.icesi.TallerJpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IcesiUserRepository  extends JpaRepository<IcesiUser, UUID> {
    Optional<IcesiUser> findByEmail(String email);
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);
}
