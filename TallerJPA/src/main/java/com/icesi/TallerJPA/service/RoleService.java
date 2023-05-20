package com.icesi.TallerJPA.service;

import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.response.IcesiRoleResponseDTO;
import com.icesi.TallerJPA.error.exception.*;
import com.icesi.TallerJPA.error.util.IcesiExceptionBuilder;
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

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    public IcesiRoleResponseDTO save(IcesiRoleDTO role) throws IcesiException {

        if(roleRepository.existsByName(role.getName())) {
            throw  eb.exceptionDuplicate("Role already exists", "role", "name", role.getName());
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleMapper.toResponse(roleRepository.save(icesiRole));
    }
}
