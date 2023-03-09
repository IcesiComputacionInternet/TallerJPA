package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.mapper.RoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public IcesiRole save(RoleCreateDTO role) {
        boolean name = roleRepository.findByName(role.getName());
        if (name) {
            throw new IllegalArgumentException("Role name already exist");
        }
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }
}
