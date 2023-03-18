package co.com.icesi.demojpa.repository;

import co.com.icesi.demojpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT u FROM IcesiUser u WHERE u.email = :email")
    Optional<IcesiUser> findByEmail(String email);

    @Query("SELECT u FROM IcesiUser u WHERE u.phoneNumber = :phone")
    Optional<IcesiUser> findByPhone(String phone);

}
