package co.com.icesi.TallerJPA.repository;

import co.com.icesi.TallerJPA.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query(value = "SELECT CASE WHEN(COUNT(*) > 0) THEN true ELSE false END FROM IcesiRole r WHERE r.name = :name")
    boolean findByName(String name);

    @Query(value = "SELECT r FROM IcesiRole r WHERE r.name = :name")
    Optional<IcesiRole> returnRole(String name);
}
