package co.com.icesi.tallerjpa.repository;

import co.com.icesi.tallerjpa.model.security.UserPermission;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PermissionRepository extends CrudRepository<UserPermission, UUID> {
}
