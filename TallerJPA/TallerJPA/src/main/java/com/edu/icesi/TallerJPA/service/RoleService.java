package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.Enums.Scopes;
import com.edu.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.edu.icesi.TallerJPA.error.exception.DetailBuilder;
import com.edu.icesi.TallerJPA.error.exception.ErrorCode;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
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

    public IcesiRoleDTO save(IcesiRoleDTO roleCreateDTO){

        verifyUserRole(IcesiSecurityContext.getCurrentRol());
        verifyRoleName(roleCreateDTO.getName());
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(roleCreateDTO);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }

    public void verifyUserRole(String roleActualUser){

        if (!roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.ADMIN))){

            throw createIcesiException(
                    "User unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();



        }
    }

    public void verifyRoleName(String roleName){

        if (roleRepository.findByName(roleName).isPresent()){

            throw createIcesiException(
                    "Role name already exists",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role", "role name", roleName)
            ).get();


        }
    }

    public IcesiRoleDTO getRoleByName(String name){
        return searchRole(name);
    }

    private IcesiRoleDTO searchRole(String name){
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

    public IcesiRoleDTO addUserToRole(IcesiRoleDTO roleCreateDTO, IcesiUser user){
        roleCreateDTO.getIcesiUsers().add(user);
        return roleCreateDTO;
    }
}
