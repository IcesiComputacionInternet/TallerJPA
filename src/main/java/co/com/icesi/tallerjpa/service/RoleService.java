package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RoleDTO;
import co.com.icesi.tallerjpa.exception.ExistsException;
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
    public Role save(RoleDTO roleDTO){

        boolean nameExists = roleRepository.existsByName(roleDTO.getName());

        if (nameExists){ throw new ExistsException("Name already exists");}

        roleDTO.setRoleId(UUID.randomUUID());
        return roleRepository.save(roleMapper.fromRoleDTO(roleDTO));

    }
}
