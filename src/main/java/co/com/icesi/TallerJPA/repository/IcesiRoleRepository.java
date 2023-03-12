package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query(value = "SELECT r FROM IcesiRole r where r.name = :name")
    Optional<IcesiRole> findByName(String name);
}
