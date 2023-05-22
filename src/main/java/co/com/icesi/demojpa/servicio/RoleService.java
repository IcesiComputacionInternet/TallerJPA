package co.com.icesi.demojpa.servicio;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.error.util.IcesiExceptionBuilder;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.userRepository = userRepository;
    }

    public RoleCreateDTO save(RoleCreateDTO role){
        if(roleRepository.findByName(role.getName()).isPresent()){
            throw new RuntimeException("Ya existe un rol con este nombre");
        }

        IcesiRole icesiRole = roleMapper.fromIcesiRoleDTO(role);
        icesiRole.setRoleId(UUID.randomUUID());
        return role;
    }

    public void addUser(IcesiRole role, UUID userid){
        IcesiUser user = userRepository.findById(userid).orElseThrow(()-> IcesiExceptionBuilder.createIcesiException("No existe un usuario con este id", HttpStatus.NOT_FOUND,"USER_NOT_FOUND"));
        role.getUser().add(user);
    }

    public Optional<IcesiRole> findById(UUID fromString){
        return roleRepository.findById(fromString);
    }
}
