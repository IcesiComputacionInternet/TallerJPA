package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.RoleDTO;
import com.edu.icesi.demojpa.mapper.RoleMapper;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public IcesiRole save(RoleDTO role) {
        boolean isNameInUse = roleRepository.findRoleByName(role.getName()).isPresent();

        if (isNameInUse) {
            throw new RuntimeException("This role is already in use");
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }
}
