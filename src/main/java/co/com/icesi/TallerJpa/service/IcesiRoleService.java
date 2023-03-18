package co.com.icesi.TallerJpa.service;

import co.com.icesi.TallerJpa.dto.IcesiRoleCreateDTO;
import co.com.icesi.TallerJpa.exceptions.icesiRoleExceptions.RoleNameAlreadyInUseException;
import co.com.icesi.TallerJpa.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;
    public IcesiRole saveRole(IcesiRoleCreateDTO roleDto) {
        if (icesiRoleRepository.findByName(roleDto.getName()).isPresent()){
            throw new RoleNameAlreadyInUseException();
        }
        IcesiRole icesiRole = icesiRoleMapper.fromRoleDto(roleDto);
        icesiRole.setRoleId(UUID.randomUUID());
        return icesiRoleRepository.save(icesiRole);
    }

    public IcesiRole getRoleByName(String name) {
        return icesiRoleRepository.findByName(name).get();
    }
}
