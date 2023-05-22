package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiPermission;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PermissionRepository extends CrudRepository<IcesiPermission, UUID> {
}