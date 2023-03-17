package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public IcesiRole save(RoleCreateDTO roleCreateDTO){

        if(roleRepository.findByName(roleCreateDTO.getName()).isPresent()){
            throw new RuntimeException("Role already exists");
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(roleCreateDTO);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleRepository.save(icesiRole);
    }
}
