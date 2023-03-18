package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID>  {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM IcesiUser u WHERE u.email = :email OR u.phoneNumber = :phoneNumber")
    boolean existsByEmailOrPhoneNumber(@Param("email") String email, @Param("phoneNumber") String phoneNumber);

    @Transactional(readOnly = true)
    @QueryHints(value = @QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
    Optional<IcesiUser> findByEmail(String email);

    Optional<Object> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
}
