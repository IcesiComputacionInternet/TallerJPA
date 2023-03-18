package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public IcesiUser save(UserCreateDTO user){

        Optional<IcesiUser> emailOptional = userRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> phoneOptional = userRepository.findByPhone(user.getPhoneNumber());
        Optional<RoleCreateDTO> roleDTOOptional = Optional.ofNullable(user.getRole());

        if(emailOptional.isPresent() && phoneOptional.isPresent()){
            throw new RuntimeException("User with both e-mail and phone already exists");
        }else if(emailOptional.isPresent()){
            throw new RuntimeException("User with this e-mail already exists");
        }else if(phoneOptional.isPresent()){
            throw new RuntimeException("User with this phone number already exists");
        }else if(!roleDTOOptional.isPresent()) {
            throw new RuntimeException("User must have a role");
        }

        Optional<IcesiRole> roleOptional = roleRepository.findByName(user.getRole().getName());

        if(roleOptional.isEmpty()){
            throw new RuntimeException("Role does not exist");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        IcesiRole role = roleOptional.get();
        icesiUser.setRole(role);
        role.getUsers().add(icesiUser);
        roleRepository.save(role);
        return userRepository.save(icesiUser);
    }
}
