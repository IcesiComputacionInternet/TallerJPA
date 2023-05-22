package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final IcesiExceptionBuilder exceptionBuilder = new IcesiExceptionBuilder();

    public RoleCreateDTO save(RoleCreateDTO role) {

        checkRolePermission();

        var roleNameExists = roleRepository.findByName(role.getName()).isPresent();

        if (roleNameExists) {
            throw exceptionBuilder.duplicatedException("Role name is not unique", role.getName());
        }
        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());

        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }

    public RoleCreateDTO getRole(String role){
        checkRolePermission();
        var roleNameExists = roleRepository.findByName(role).isPresent();
        if(roleNameExists) {
            return roleMapper.fromIcesiRole(roleRepository.findByName(role).get());
        }
        throw exceptionBuilder.notFoundException("Role does not exist", "Role", "name", role);
    }

    public List<RoleCreateDTO> getAllRoles(){
        List<IcesiRole> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::fromIcesiRole).collect(Collectors.toList());
    }

    public void checkRolePermission() {
        String roleToken = IcesiSecurityContext.getCurrentRole();

        if(!roleToken.equals("ADMIN")) {
            throw exceptionBuilder.forbiddenException("You can't create or assign a role");
        }

    }


}
