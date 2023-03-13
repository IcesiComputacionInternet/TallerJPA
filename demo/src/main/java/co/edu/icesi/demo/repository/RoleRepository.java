package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {
}
