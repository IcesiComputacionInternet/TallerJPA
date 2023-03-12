package com.example.TallerJPA.repository;

import com.example.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<IcesiUser, UUID> {

}
