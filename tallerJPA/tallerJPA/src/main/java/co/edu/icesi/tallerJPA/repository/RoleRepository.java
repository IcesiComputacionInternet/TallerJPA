package co.edu.icesi.tallerJPA.repository;

import co.edu.icesi.tallerJPA.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT CASE WHEN (COUNT(r) > 0) THEN true ELSE false END FROM Role r WHERE r.name = :name")
    boolean isByname(String name);

    @Query("SELECT role FROM Role role WHERE role.name = :name")
    Optional<Role> findByName(String name);
}

