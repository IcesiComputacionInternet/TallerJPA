package co.com.icesi.demojpa.repository;

import co.com.icesi.demojpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {


    Optional<IcesiUser> findByEmail(String fromString);

    Optional<IcesiUser> findByPhone(String fromString);
}
