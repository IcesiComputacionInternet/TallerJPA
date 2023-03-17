package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.mapper.RoleMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    private RoleMapper roleMapper;

    public IcesiRole save(RoleCreateDTO role){
        if(roleRepository.findByName(role.getName()).isPresent()){
            throw new RuntimeException("Role name already exists");
        }
        IcesiRole icesiRole=roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }

}
