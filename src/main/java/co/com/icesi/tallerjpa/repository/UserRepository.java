package co.com.icesi.tallerjpa.repository;

import co.com.icesi.tallerjpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.phoneNumber = :phoneNumber")
    boolean existsByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT u FROM IcesiUser u WHERE u.email = :email")
    Optional<IcesiUser> findByEmail(@Param("email") String email);

}
