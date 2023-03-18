package co.com.icesi.icesiAccountSystem.repository;

import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {
    @Query("SELECT role FROM IcesiRole role WHERE role.name = :name")
    Optional<IcesiRole> findByName(String name);
}
