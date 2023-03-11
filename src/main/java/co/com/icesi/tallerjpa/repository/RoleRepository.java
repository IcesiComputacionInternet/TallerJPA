package co.com.icesi.tallerjpa.repository;

import co.com.icesi.tallerjpa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END FROM Role u WHERE u.name = :name")
    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Role findByName(String name);
}
