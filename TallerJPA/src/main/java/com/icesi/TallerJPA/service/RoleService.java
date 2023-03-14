package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.icesi.TallerJPA.mapper.RoleMapper;
import com.icesi.TallerJPA.model.IcesiRole;
import com.icesi.TallerJPA.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    public IcesiRole save(IcesiRoleDTO role) {

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }
}
