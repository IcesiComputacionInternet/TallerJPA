package co.com.icesi.demojpa.repository;


import co.com.icesi.demojpa.model.IcesiPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<IcesiPermission, UUID> {

}