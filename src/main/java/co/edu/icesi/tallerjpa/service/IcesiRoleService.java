package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.exception.DataAlreadyExist;
import co.edu.icesi.tallerjpa.exception.DataAlreadyExistException;
import co.edu.icesi.tallerjpa.exception.InvalidDataException;
import co.edu.icesi.tallerjpa.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@AllArgsConstructor
public class IcesiRoleService {



        private final IcesiRoleRepository icesiRoleRepository;

        private final IcesiRoleMapper icesiRoleMapper;

    public IcesiRole createRole(IcesiRoleDTO roleDTO) throws InvalidDataException, DataAlreadyExistException {
        String roleName = roleDTO.getName().trim();

        if (roleName.isEmpty()) {
            throw new InvalidDataException("The role name cannot be empty");
        }

        if (icesiRoleRepository.findByName(roleName).isPresent()) {
            throw new DataAlreadyExistException("There is already a role with the name " + roleName);
        }

        IcesiRole role = icesiRoleMapper.toIcesiRole(roleDTO);
        role.setRoleId(UUID.randomUUID());

        return icesiRoleRepository.save(role);
    }



    private void validateRole(IcesiRole icesiRole) throws DataAlreadyExist {
        if (icesiRoleRepository.existsByName(icesiRole.getName())) {
            throw new DataAlreadyExist("El nombre del rol ya existe");
        }
    }


}
