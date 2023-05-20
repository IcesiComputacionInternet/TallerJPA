package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO saveRole(RoleDTO roleDTO){
        if(roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw new RuntimeException("Another role already has this name.");
        }
        IcesiRole icesiRole = roleMapper.fromRoleDTO(roleDTO);
        icesiRole.setRoleId(UUID.randomUUID());
        roleRepository.save(icesiRole);
        return roleMapper.fromRoleToRoleDTO(icesiRole);
    }

    public RoleDTO getRole(String roleName) {
        Optional<IcesiRole> roleByName=roleRepository.findByName(roleName);
        if (!roleByName.isPresent()){
            throw new RuntimeException("The role with the specified email does not exists.");
        }
        return roleMapper.fromRoleToRoleDTO(roleByName.get());
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::fromRoleToRoleDTO).collect(Collectors.toList());
    }
}
