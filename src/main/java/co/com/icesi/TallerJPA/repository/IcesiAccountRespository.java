package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IcesiAccountRespository extends JpaRepository<IcesiAccount, UUID> {
}
