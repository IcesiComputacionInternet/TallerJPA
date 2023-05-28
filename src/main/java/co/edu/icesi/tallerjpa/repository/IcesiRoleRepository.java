package co.edu.icesi.tallerjpa.repository;

import co.edu.icesi.tallerjpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    Optional<IcesiRole> findByName(String name);
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM IcesiRole r WHERE r.name = ?1")
    boolean existsByName(String name);

    List<IcesiRole> findByDescription(String description);
}
