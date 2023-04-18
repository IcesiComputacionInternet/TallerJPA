package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.RoleCreateDTO;
import co.com.icesi.TallerJPA.error.util.DetailBuilder;
import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import co.com.icesi.TallerJPA.error.util.ArgumentsExceptionBuilder;
import co.com.icesi.TallerJPA.mapper.RoleMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public IcesiRole save(RoleCreateDTO role) {
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
        return roleRepository.save(icesiRole);
    }

    public IcesiRole getRoleByName(String roleName) {
        return roleRepository.returnRole(roleName).orElse(null);
    }

    public List<IcesiRole> getAllRoles() {
        return roleRepository.findAll();
    }
}
