package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query("SELECT r FROM IcesiRole r WHERE r.name = :name")
    Optional<IcesiRole> findByName(String name);
}
