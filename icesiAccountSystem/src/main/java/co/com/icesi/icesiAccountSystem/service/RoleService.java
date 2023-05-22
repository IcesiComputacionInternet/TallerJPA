package co.com.icesi.icesiAccountSystem.service;

import co.com.icesi.icesiAccountSystem.dto.RoleDTO;
import co.com.icesi.icesiAccountSystem.enums.ErrorCode;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemException;
import co.com.icesi.icesiAccountSystem.error.exception.DetailBuilder;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder.createAccountSystemException;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleDTO saveRole(RoleDTO roleDTO){
        checkPermissions();
        if(roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw createAccountSystemException(
                    "Another role already has this name.",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role", "name", roleDTO.getName())
            ).get();
        }
        IcesiRole icesiRole = roleMapper.fromRoleDTO(roleDTO);
        icesiRole.setRoleId(UUID.randomUUID());
        roleRepository.save(icesiRole);
        return roleMapper.fromRoleToRoleDTO(icesiRole);
    }

    public RoleDTO getRole(String roleName) {
        checkPermissions();
        var roleByName=roleRepository.findByName(roleName)
                .orElseThrow(
                        createAccountSystemException(
                                "The role with the specified name does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Role", "name", roleName)
                        )
                );
        return roleMapper.fromRoleToRoleDTO(roleByName);
    }

    private void checkPermissions() {
        if(!IcesiSecurityContext.getCurrentUserRole().equals("ADMIN")){
            throw createAccountSystemException(
                    "Only an ADMIN user can create and see created roles.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::fromRoleToRoleDTO).collect(Collectors.toList());
    }
}
