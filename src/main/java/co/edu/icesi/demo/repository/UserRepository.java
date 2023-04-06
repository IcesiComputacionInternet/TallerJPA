package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT user FROM IcesiUser user WHERE  user.email= :email")
    Optional<IcesiUser> findByEmail(String email);

    @Query("SELECT user FROM IcesiUser user WHERE  user.phoneNumber= :phoneNumber")
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

}
