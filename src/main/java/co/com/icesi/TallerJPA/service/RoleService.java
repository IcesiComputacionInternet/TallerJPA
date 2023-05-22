package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.AssignRoleDTO;
import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.error.util.DetailBuilder;
import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import co.com.icesi.TallerJPA.error.util.ArgumentsExceptionBuilder;
import co.com.icesi.TallerJPA.mapper.RoleMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;

    public RoleCreateDTO save(RoleCreateDTO role) {
        validateRoleName();

        boolean name = roleRepository.findByName(role.getName());
        if (name) {
            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Existing data",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_406,"Role name")
            );
            //throw new ArgumentsException("Role name already exist");
        }
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleMapper.fromICesiRole(roleRepository.save(icesiRole));


    }

    private void validateRoleName() {
        var role = IcesiSecurityContext.getCurrentUserRole();

        if (!role.equals("ADMIN")){
            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            );
            //throw new ArgumentsException("Unauthorized");
        }
    }

    @Transactional
    public UserResponseDTO assignRole(AssignRoleDTO assignRoleDTO){
        validateRoleName();
        IcesiRole role = validateExistingRole(assignRoleDTO.roleName());
        IcesiUser user = validateExistingUser(assignRoleDTO.username());

        user.setRole(role);
        userRepository.updateRole(user.getEmail(),role);
        return userResponseMapper.fromICesiUSer(user);
    }

    private IcesiRole validateExistingRole(String roleName){
        return roleRepository.returnRole(roleName).orElseThrow(
                ArgumentsExceptionBuilder.createArgumentsExceptionSup(
                        "Role not found",
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_NOT_FOUND,"Role")
                )
        );

    }

    private IcesiUser validateExistingUser(String username){
        return userRepository.findUserByEmail(username).orElseThrow(
                ArgumentsExceptionBuilder.createArgumentsExceptionSup(
                "User not found",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_NOT_FOUND,"User")
        ));
    }

    public RoleCreateDTO getRoleByName(String roleName) {
        return roleMapper.fromICesiRole(roleRepository.returnRole(roleName).orElse(null));
    }

    public List<IcesiRole> getAllRoles() {
        return roleRepository.findAll();
    }
}
