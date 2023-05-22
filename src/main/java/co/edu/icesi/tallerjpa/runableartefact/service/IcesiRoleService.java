package co.edu.icesi.tallerjpa.runableartefact.service;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.RoleToAssignDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService{

    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiUserRepository icesiUserRepository;

    private final AuthoritiesService authoritiesService;

    private final IcesiRoleMapper icesiRoleMapper;

    public String saveNewRole(IcesiRoleDTO icesiRoleDTO){

        IcesiRole icesiRole = icesiRoleMapper.toIcesiRole(icesiRoleDTO);
        validateRole(icesiRole);
        authoritiesService.validateAuthorities("ADMIN");

        icesiRole.setRoleId(UUID.randomUUID());
        icesiRoleRepository.save(icesiRole);
        return "Role saved";

    }

    public IcesiRoleDTO assignRoleToIcesiUser(RoleToAssignDTO roleToAssignDTO){
        icesiUserRepository.findById(roleToAssignDTO.getUserId()).ifPresent(icesiUser -> {
            Optional<IcesiRole> icesiRole = icesiRoleRepository.findByName(roleToAssignDTO.getRoleName());
            icesiUser.setRole(icesiRole.orElseThrow(() -> new RuntimeException("Role not found")));
            icesiUserRepository.save(icesiUser);
        });
        return icesiRoleRepository.findByName(roleToAssignDTO.getRoleName()).map(icesiRoleMapper::toIcesiRoleDTO).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    private void validateRole(IcesiRole icesiRole){
        boolean nameAlreadyExist = icesiRoleRepository.existsByName(icesiRole.getName());
        if (nameAlreadyExist) {throw new DataAlreadyExist("Role name already exists");}
    }
}
