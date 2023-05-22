package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.request.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
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
    public RoleCreateDTO save(RoleCreateDTO role){
        if(roleRepository.existsByName(role.getName())){
            throw new RuntimeException("Role already exists");
        }

        IcesiRole icesiRole = roleMapper.fromRoleCreateDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleMapper.fromIcesiRole(roleRepository.save(icesiRole));
    }

}
