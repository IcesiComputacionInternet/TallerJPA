package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query(value = "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.email = :email")
    boolean findByEmail(String email);

    @Query(value = "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.phoneNumber = :phoneNumber")
    boolean findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT u FROM IcesiUser u WHERE u.email = :email")
    Optional<IcesiUser> findUserByEmail(String email);


}
