package com.icesi.TallerJPA.repository;

import com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

}
