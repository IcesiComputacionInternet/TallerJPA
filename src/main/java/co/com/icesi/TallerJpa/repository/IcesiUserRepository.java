package co.com.icesi.TallerJpa.repository;

import co.com.icesi.TallerJpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IcesiUserRepository  extends JpaRepository<IcesiUser, UUID> {
    @Query( "SELECT iu " +
            "FROM IcesiUser iu " +
            "WHERE iu.email = :email")
    Optional<IcesiUser> findByEmail(@Param("email") String email);

    @Query( "SELECT iu " +
            "FROM IcesiUser iu " +
            "WHERE iu.phoneNumber = :phoneNumber")
    Optional<IcesiUser> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
