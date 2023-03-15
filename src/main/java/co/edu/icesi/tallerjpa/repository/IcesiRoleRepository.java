package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {
    Optional<IcesiRole> findByName(String name);
}
