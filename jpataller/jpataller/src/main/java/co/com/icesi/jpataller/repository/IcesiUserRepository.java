package co.com.icesi.jpataller.repository;

import co.com.icesi.jpataller.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    Optional<IcesiUser> findByEmail(String emailString);

    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

}
