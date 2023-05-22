package co.edu.icesi.tallerjpa.repository;


import co.edu.icesi.tallerjpa.model.IcesiPermits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IcesiPermissionRepository extends JpaRepository<IcesiPermits, UUID> {

    @Query("SELECT p FROM IcesiPermits p")
    List<IcesiPermits> findAll();


}
