package com.example.demo.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.IcesiUser;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
    
    Optional<IcesiUser> findByEmail(String email);
    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

}
