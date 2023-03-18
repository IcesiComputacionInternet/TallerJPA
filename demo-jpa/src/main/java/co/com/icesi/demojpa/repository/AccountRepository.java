package co.com.icesi.demojpa.repository;

import co.com.icesi.demojpa.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<IcesiAccount, UUID> {
}
