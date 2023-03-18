package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    public IcesiRole save(RoleCreateDTO role){
        if(roleRepository.findByName(role.getName()).isPresent()){
            throw new RuntimeException("Role already exists");
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return roleRepository.save(icesiRole);
    }

    public void addUserToRole(IcesiRole role, UUID userId){
        if(userRepository.findById(userId).isPresent()){
            role.getIcesiUsers().add(userRepository.findById(userId).get());
        }else{
            throw new RuntimeException("User doesn't exists");
        }
    }

    public Optional<IcesiRole> fingById(UUID fromString) {

        return roleRepository.findById(fromString);
    }

}
