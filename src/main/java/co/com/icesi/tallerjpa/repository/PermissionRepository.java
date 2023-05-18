package co.com.icesi.tallerjpa.repository;

import co.com.icesi.tallerjpa.model.security.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<UserPermission, UUID> {
}
