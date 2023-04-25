package com.example.demo.repository;

import com.example.demo.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT u FROM IcesiUser u WHERE u.email = :email")
    IcesiUser findByEmail(String email);

    @Query("SELECT u FROM IcesiUser u WHERE u.phoneNumber = :phoneNumber")
    IcesiUser findByPhoneNumber(String phoneNumber);
}
