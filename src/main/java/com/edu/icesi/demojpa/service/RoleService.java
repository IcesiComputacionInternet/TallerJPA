package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.RoleCreateDTO;
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

    public IcesiRole save(RoleCreateDTO role){
        String name = role.getName();

        if(isNameInUse(name)){
            fieldIsRepeatedException("name");
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(roleIdGenerator());

        return roleRepository.save(icesiRole);
    }

    public UUID roleIdGenerator(){
        return UUID.randomUUID();
    }

    public boolean isNameInUse(String name){
        return roleRepository.findRoleByName(name).isPresent();
    }

    private void fieldIsRepeatedException(String field) {
        throw new RuntimeException("The "+field+" is already in use");
    }
}
