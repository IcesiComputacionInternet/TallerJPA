package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<IcesiUser, UUID> {
    @Query("select u from IcesiUser u where u.email = ?1")
    Optional<IcesiUser> findByEmail(String email);

    @Query("select u from IcesiUser u where u.phoneNumber = ?1")
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

}
