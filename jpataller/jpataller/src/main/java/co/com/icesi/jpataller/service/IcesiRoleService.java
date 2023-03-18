package co.com.icesi.jpataller.service;

import co.com.icesi.jpataller.dto.IcesiRoleDTO;
import co.com.icesi.jpataller.mapper.IcesiRoleMapper;
import co.com.icesi.jpataller.model.IcesiRole;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiRoleRepository;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleMapper icesiRoleMapper;

    public IcesiRole createRole(IcesiRoleDTO icesiRoleDTO) {
        if (icesiRoleRepository.findByName(icesiRoleDTO.getName()).isPresent()) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }
        IcesiRole icesiRole = icesiRoleMapper.fromDTO(icesiRoleDTO);
        icesiRole.setRoleId(UUID.randomUUID());
        return icesiRoleRepository.save(icesiRole);
    }

    public void addUserToRole(IcesiRole icesiRole, UUID userId) {
        Optional<IcesiUser> icesiUser = icesiUserRepository.findById(userId);
        if (icesiUser.isEmpty()) {
            throw new RuntimeException("No existe un usuario con este id");
        }
        icesiRole.getUsers().add(icesiUser.get());
    }
}
