package co.edu.icesi.tallerjpa.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleShowDTO;
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

    public IcesiRoleShowDTO save(IcesiRoleCreateDTO icesiRoleDTO){
        if(!isRoleNameUnique(icesiRoleDTO.getName())){
            throw new RuntimeException("There is already a role with the name: " + icesiRoleDTO.getName());
        }
        IcesiRole icesiRole = icesiRoleMapper.fromCreateIcesiRoleDTO(icesiRoleDTO);
        icesiRole.setRoleId(UUID.randomUUID());
        return icesiRoleMapper.fromIcesiRoleToShowDTO(icesiRoleRepository.save(icesiRole));
    }

    private boolean isRoleNameUnique(String roleName){
        if(icesiRoleRepository.findByName(roleName).isPresent()){
            return false;
        }
        return true;
    }

}
