package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {

}
