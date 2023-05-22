package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);

    Optional<IcesiUser> findIcesiUserByEmail(String email);

    @Transactional(readOnly = true)
    @QueryHints(value = @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Optional<IcesiUser> findByEmail(String email);

    Optional<Object> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}

