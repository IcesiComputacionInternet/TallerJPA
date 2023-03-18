package com.example.tallerjpa.repository;

import com.example.tallerjpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository <IcesiUser, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.phoneNumber = :phoneNumber")
    boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiUser u WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT u FROM IcesiUser u WHERE u.email = :email")
    Optional<IcesiUser> searchByEmail(@Param("email") String email);

}
