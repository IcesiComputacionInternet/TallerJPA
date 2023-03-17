package com.icesi.TallerJPA.repository;

import com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRespository extends JpaRepository<IcesiUser, UUID> {

    @Query("SELECT u FROM IcesiUser u where u.email = :email")
    Optional<IcesiUser> findIcesiUserByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END AS userExist FROM IcesiUser u WHERE u.email = :email")
    Boolean existsByEmail(@Param("email")String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END AS phoneExist FROM IcesiUser u WHERE u.phoneNumber = :phoneNumber")
    Boolean existsByPhoneNumber(@Param("phoneNumber")String phoneNumber);

}
