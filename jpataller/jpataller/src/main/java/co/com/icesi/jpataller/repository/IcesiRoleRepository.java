package co.com.icesi.jpataller.repository;

import co.com.icesi.jpataller.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    Optional<IcesiRole> findByName(String roleName);

}
