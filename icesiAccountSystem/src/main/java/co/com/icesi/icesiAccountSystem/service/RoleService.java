package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public IcesiRole saveRole(RoleDTO roleDTO){
        if(roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw new RuntimeException("Another role already has this name.");
        }
        IcesiRole icesiRole = roleMapper.fromRoleDTO(roleDTO);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleRepository.save(icesiRole);
    }
}
