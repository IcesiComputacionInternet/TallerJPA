package com.example.TallerJPA.service;

import com.example.TallerJPA.dto.RoleCreateDTO;
import com.example.TallerJPA.mapper.RoleMapper;
import com.example.TallerJPA.model.IcesiRole;
import com.example.TallerJPA.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    public IcesiRole save(RoleCreateDTO role) {
        Optional<IcesiRole> roleFound = roleRepository.findByName(role.getName());
        if(roleFound.isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }
}
