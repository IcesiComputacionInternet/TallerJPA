package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {
}
