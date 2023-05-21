package co.com.icesi.TallerJpa.repository;

import co.com.icesi.TallerJpa.model.IcesiRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IcesiRoleRepository extends JpaRepository<IcesiRole, UUID> {

    @Query( "SELECT ir " +
            "FROM IcesiRole ir " +
            "WHERE ir.name = :name")
    Optional<IcesiRole> findByName(@Param("name") String name);

    @Query( "SELECT CASE WHEN (COUNT(ir) > 0) " +
            "THEN true " +
            "ELSE false END " +
            "FROM IcesiRole ir " +
            "WHERE ir.name = :name")
    boolean existsByName(@Param("name") String name);
}
