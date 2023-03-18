package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.RoleCreateDTO;
import com.edu.icesi.demojpa.mapper.RoleMapper;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public IcesiRole save(RoleCreateDTO role){
        verifyFieldInUse(role);
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(idGenerator());
        return roleRepository.save(icesiRole);
    }

    public void verifyFieldInUse(RoleCreateDTO role){
        String name = role.getName();

        if(isNameInUse().test(name)){
            fieldIsRepeatedException("name");
        }
    }

    public UUID idGenerator(){
        return UUID.randomUUID();
    }

    public Predicate<String> isNameInUse(){
        return (name) -> roleRepository.findRoleByName(name).isPresent();
    }

    private void fieldIsRepeatedException(String field) {
        throw new RuntimeException("The "+field+" is already in use");
    }
}
