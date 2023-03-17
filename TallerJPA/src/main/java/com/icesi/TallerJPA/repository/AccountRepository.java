package com.icesi.TallerJPA.repository;

import com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {

    @Query("SELECT CASE WHEN COUNT(a)>0 THEN true ELSE false END FROM IcesiAccount a WHERE a.accountNumber =:accountNumber")
    Boolean existsByAccountNumber (@Param("accountNumber") String accountNumber);

}
