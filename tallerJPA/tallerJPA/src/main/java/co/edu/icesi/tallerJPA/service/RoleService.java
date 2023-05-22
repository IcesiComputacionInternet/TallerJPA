package co.edu.icesi.tallerJPA.service;

import co.edu.icesi.tallerJPA.dto.RoleDTO;
import co.edu.icesi.tallerJPA.exception.ExistingException;
import co.edu.icesi.tallerJPA.mapper.RoleMapper;
import co.edu.icesi.tallerJPA.model.Role;
import co.edu.icesi.tallerJPA.repository.RoleRepository;
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

        boolean checkName = roleRepository.isByname(roleDTO.getName());

        if (checkName) throw new ExistingException("Role name exists, please provide other");

        roleDTO.setRoleId(UUID.randomUUID());
        return roleRepository.save(roleMapper.fromRoleDTO(roleDTO));
    }
}
