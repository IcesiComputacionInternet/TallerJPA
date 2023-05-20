package com.icesi.TallerJPA.repository;

import com.icesi.TallerJPA.model.IcesiPermits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<IcesiPermits, UUID> {

    @Query("SELECT p FROM IcesiPermits p")
    List<IcesiPermits> findAll();


}
