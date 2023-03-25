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

    public RoleCreateDTO save(RoleCreateDTO roleCreateDTO){

        roleRepository.findByName(roleCreateDTO.getName()).orElseThrow(() -> new RuntimeException("The role "+roleCreateDTO.getName()+" already exists"));

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(roleCreateDTO);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }

    public RoleCreateDTO getRoleByName(String name){
        return roleMapper.fromIcesiRole(roleRepository.findByName(name).orElseThrow(()-> new RuntimeException("Role not found")));
    }
}
