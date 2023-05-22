package com.Icesi.TallerJPA.repository;

import com.Icesi.TallerJPA.model.IcesiUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
    @NonNull
    @Query("SELECT user FROM IcesiUser user")
    List<IcesiUser> findAll();

    Optional<IcesiUser> findByEmail(String email);

    @Query("SELECT user FROM IcesiUser user WHERE user.phoneNumber =: phoneNumber")
    Optional<IcesiUser> finByPhoneNumber(String phoneNumber);


}
