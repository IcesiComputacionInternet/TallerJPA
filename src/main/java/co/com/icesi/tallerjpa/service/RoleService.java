package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.repository.RoleRepository;
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

    public RoleCreateDTO save(RoleCreateDTO role) {
        var roleNameExists = roleRepository.findByName(role.getName()).isPresent();

        if (roleNameExists) {
            throw new RuntimeException("Role name is not unique");
        }
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }

    public RoleCreateDTO getRole(String role){
        var roleNameExists = roleRepository.findByName(role).isPresent();
        if(roleNameExists) {
            return roleMapper.fromIcesiRole(roleRepository.findByName(role).get());
        }
        throw new RuntimeException("Role does not exist");
    }

    public List<RoleCreateDTO> getAllRoles(){
        List<IcesiRole> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::fromIcesiRole).collect(Collectors.toList());
    }


}
