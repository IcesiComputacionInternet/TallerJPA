package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public IcesiRole save(RoleCreateDTO role){
        Optional<IcesiRole> roleOptional = Optional.ofNullable(roleRepository.findByName(role.getName()));
        if(roleOptional.isPresent()){
            throw new RuntimeException("Role name already exists");
        }

        IcesiRole icesiRole= roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }

}
