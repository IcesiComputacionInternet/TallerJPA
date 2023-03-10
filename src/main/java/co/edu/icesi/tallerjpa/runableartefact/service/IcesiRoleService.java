package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.exception.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;

    public String saveNewRole(IcesiRole icesiRole) throws DataAlreadyExist {

        boolean nameAlreadyExist = icesiRoleRepository.existsByName(icesiRole.getName());
        if (nameAlreadyExist) {throw new DataAlreadyExist("Role name already exists");}

        icesiRole.setRoleId(UUID.randomUUID());
        icesiRoleRepository.save(icesiRole);
        return "Role saved";

    }
}
