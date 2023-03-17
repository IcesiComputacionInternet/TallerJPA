package com.example.jpa.repository;

import com.example.jpa.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

    Optional<IcesiUser> findByEmail(String email);

    Optional<IcesiUser> findByPhoneNumber(String phoneNumber);

}
