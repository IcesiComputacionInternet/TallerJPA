package icesi.university.accountSystem.services;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.mapper.IcesiRoleMapper;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private IcesiRoleRepository icesiRoleRepository;

    private IcesiRoleMapper icesiRoleMapper;

    public IcesiRole save(IcesiRoleDTO role){
        if(icesiRoleRepository.findById(role.getRoleId()).isPresent()){
            throw new RuntimeException("Role already exists");
        }else if(icesiRoleRepository.findByName(role.getName()).isPresent()){
            throw new RuntimeException("This name of role already exists");
        }

        IcesiRole icesiRole = icesiRoleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return icesiRoleRepository.save(icesiRole);
    }

    public List<IcesiRole> getAllRoles() {
        return icesiRoleRepository.findAll();
    }

}
