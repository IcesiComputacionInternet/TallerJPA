package com.Icesi.TallerJPA.repository;

import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query("SELECT role FROM IcesiRole role WHERE role.name = :name")
   boolean findExistName(String name);

    Optional<IcesiRole> findByName(String name);


}
