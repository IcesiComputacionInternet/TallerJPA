package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query(value = "SELECT u FROM IcesiUser u where u.email= :email")
    Optional<IcesiUser> findByEmail(String email);

    @Query(value = "SELECT u FROM IcesiUser u where u.phoneNumber= :phone")
    Optional<IcesiUser> findByPhoneNumber(String phone);

    @Query(value = "SELECT u FROM IcesiUser u where u.firstName= :name")
    Optional<IcesiUser> findbyName(String name);



}
