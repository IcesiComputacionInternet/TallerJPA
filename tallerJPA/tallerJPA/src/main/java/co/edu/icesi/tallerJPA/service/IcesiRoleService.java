package co.edu.icesi.tallerJPA.service;

import co.edu.icesi.tallerJPA.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerJPA.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerJPA.model.IcesiRole;
import co.edu.icesi.tallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;

    public IcesiRole save(IcesiRoleCreateDTO role){
        if(icesiRoleRepository.findByName(role.getName()).isPresent()){
            throw new RuntimeException("This rol already exists");
        }
        IcesiRole icesiRole = icesiRoleMapper.fromIcesiRoleCreateDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        icesiRole.setUsers(new ArrayList<>());
        return icesiRoleRepository.save(icesiRole);
    }
}
