package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.request.RoleCreateDTO;
import co.com.icesi.demojpa.dto.response.RoleResponseDTO;
import co.com.icesi.demojpa.error.exception.IcesiException;
import co.com.icesi.demojpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class RoleService {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    private final IcesiExceptionBuilder eb = new IcesiExceptionBuilder();

    public RoleResponseDTO save(RoleCreateDTO role) throws IcesiException {

        if(roleRepository.existsByName(role.getName())) {
            throw  eb.exceptionDuplicate("Role already exists", "role", "name", role.getName());
        }

        IcesiRole icesiRole = roleMapper.fromRoleCreateDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }
}
