package com.Icesi.TallerJPA.repository;

import com.Icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT user FROM IcesiUser user WHERE user.email =: email")
    Optional<IcesiUser> finByEmail(String email);
    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber =: phoneNumber")
    Optional<IcesiUser> finByPhoneNumber(String phoneNumber);

}
