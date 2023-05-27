package com.edu.icesi.demojpa.repository;

import com.edu.icesi.demojpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT user FROM IcesiUser user WHERE user.email = :email")
    Optional<IcesiUser> isEmailInUse(@Param("email") String email);

    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber = :phoneNumber")
    Optional<IcesiUser> isPhoneNumberInUse(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT user FROM IcesiUser user WHERE user.id = :id")
    Optional<IcesiUser> findUserById(@Param("id") UUID id);
}
