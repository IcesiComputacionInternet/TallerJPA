package com.edu.icesi.demojpa.repository;

import com.edu.icesi.demojpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID>{
}
