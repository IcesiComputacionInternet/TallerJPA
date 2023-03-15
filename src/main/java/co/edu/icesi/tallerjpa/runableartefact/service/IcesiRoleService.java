package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiRoleMapper icesiRoleMapper;

    public String saveNewRole(IcesiRoleDTO icesiRoleDTO){

        IcesiRole icesiRole = icesiRoleMapper.toIcesiRole(icesiRoleDTO);
        validateRole(icesiRole);

        icesiRole.setRoleId(UUID.randomUUID());
        icesiRoleRepository.save(icesiRole);
        return "Role saved";

    }

    private void validateRole(IcesiRole icesiRole){
        boolean nameAlreadyExist = icesiRoleRepository.existsByName(icesiRole.getName());
        if (nameAlreadyExist) {throw new DataAlreadyExist("Role name already exists");}
    }
}
