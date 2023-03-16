package com.icesi.TallerJPA.repository;

import com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRespository extends JpaRepository<IcesiUser, UUID> {


}
