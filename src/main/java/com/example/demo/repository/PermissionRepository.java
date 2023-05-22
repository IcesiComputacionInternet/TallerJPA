package com.example.demo.repository;

import com.example.demo.model.IcesiPermission;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface PermissionRepository extends CrudRepository<IcesiPermission, UUID> {
    
}
