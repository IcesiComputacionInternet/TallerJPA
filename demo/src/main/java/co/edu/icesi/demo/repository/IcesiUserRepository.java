package co.edu.icesi.demo.repository;

import co.edu.icesi.demo.model.IcesiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IcesiUserRepository extends JpaRepository<IcesiUser, UUID> {
}
