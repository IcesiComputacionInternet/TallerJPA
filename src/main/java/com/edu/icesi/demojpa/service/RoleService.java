package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.Security.IcesiSecurityContext;
import com.edu.icesi.demojpa.dto.request.RoleDTO;
import com.edu.icesi.demojpa.error.util.IcesiExceptionBuilder;
import com.edu.icesi.demojpa.mapper.RoleMapper;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final IcesiExceptionBuilder icesiExceptionBuilder = new IcesiExceptionBuilder();

    public RoleDTO save(RoleDTO role) {
        hasPermission(IcesiSecurityContext.getCurrentRole());
        boolean isNameInUse = roleRepository.findRoleByName(role.getName()).isPresent();

        if (isNameInUse) {
            throw icesiExceptionBuilder.duplicatedValueException("This role is already in use", role.getName());
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        roleRepository.save(icesiRole);
        return roleMapper.fromIcesiRole(icesiRole);
    }

    private void hasPermission(String currentRole) {
        if(!currentRole.equalsIgnoreCase("ADMIN")){
            throw icesiExceptionBuilder.noPermissionException("No permission to do that");
        }
    }

    public RoleDTO getRole(String roleName){
        return roleMapper.fromIcesiRole(
                roleRepository.findRoleByName(roleName)
                        .orElseThrow(() -> icesiExceptionBuilder.notFoundException("The role with the name " + roleName + "doesn't exists", roleName)));
    }

    public List<RoleDTO> getAllRoles(){
        return roleRepository
                .findAll()
                .stream()
                .map(roleMapper::fromIcesiRole)
                .collect(Collectors.toList());
    }
}
