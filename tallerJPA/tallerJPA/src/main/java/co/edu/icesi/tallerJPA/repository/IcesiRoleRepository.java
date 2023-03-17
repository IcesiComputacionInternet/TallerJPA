package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    Optional<IcesiRole> findByName(String name);
}
