package com.example.demo.Repository;

import com.example.demo.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
    IcesiUser findByEmail(String email);

    IcesiUser findByPhoneNumber(String phoneNumber);
}
