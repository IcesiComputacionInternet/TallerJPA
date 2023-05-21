package icesi.university.accountSystem.repository;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query("SELECT role FROM IcesiRole role WHERE role.name = :name")
    Optional<IcesiRole> findByName(String name);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM IcesiRole u WHERE u.name = :name")
    boolean existsByName(String name);
}
