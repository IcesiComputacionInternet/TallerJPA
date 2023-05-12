package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.error.exception.CustomException;
import co.com.icesi.tallerjpa.mapper.RoleMapper;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @SneakyThrows
    public RoleDTO save(RoleDTO roleDTO){

        boolean nameExists = roleRepository.existsByName(roleDTO.name());

        if (nameExists){ throw new CustomException("Name already exists");}

        Role role = roleMapper.fromRoleDTO(roleDTO);
        role.setRoleId(UUID.randomUUID());
        return roleMapper.fromRole(roleRepository.save(role));

    }
}
