package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.error.exception.DetailBuilder;
import com.edu.icesi.TallerJPA.error.exception.ErrorCode;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.edu.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleCreateDTO save(RoleCreateDTO roleCreateDTO){

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(roleCreateDTO);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }

    public RoleCreateDTO getRoleByName(String name){
        return searchRole(name);
    }

    private RoleCreateDTO searchRole(String name){
        Optional<IcesiRole> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            throw createIcesiException(
                    "Role not found",
                    HttpStatus.NOT_FOUND,
                    new DetailBuilder(ErrorCode.ERR_404, "role", "role name", name)
            ).get();
        }
        return roleMapper.fromIcesiRole(role.get());
    }
}
