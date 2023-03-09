package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query(value = "select u.email from IcesiUser u")
    List<String> findByEmail();

    @Query(value = "select u.phoneNumber from IcesiUser u")
    List<String> findByPhoneNumber();
}
