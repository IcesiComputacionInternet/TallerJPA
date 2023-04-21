package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.dto.RoleDTO;
import com.edu.icesi.demojpa.mapper.RoleMapper;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO save(RoleDTO role) {
        boolean isNameInUse = roleRepository.findRoleByName(role.getName()).isPresent();

        if (isNameInUse) {
            throw new RuntimeException("This role is already in use");
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        roleRepository.save(icesiRole);
        return roleMapper.fromIcesiRole(icesiRole);
    }

    public RoleDTO getRole(String roleName){
        return roleMapper.fromIcesiRole(
                roleRepository.findRoleByName(roleName)
                        .orElseThrow(() -> new RuntimeException("The role with the name " + roleName + "doesn't exists")));
    }

    public List<RoleDTO> getAllRoles(){
        return roleRepository
                .findAll()
                .stream()
                .map(roleMapper::fromIcesiRole)
                .collect(Collectors.toList());
    }
}
