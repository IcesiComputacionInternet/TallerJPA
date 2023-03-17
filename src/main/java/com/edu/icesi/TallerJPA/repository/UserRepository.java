package com.edu.icesi.TallerJPA.repository;

import com.edu.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

    @Query("SELECT user FROM IcesiUser user WHERE user.email = :email")
    Optional<IcesiUser> findByEmail(String email);
}
