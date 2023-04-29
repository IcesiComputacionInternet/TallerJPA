package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.RoleMapper;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserCreateDTO save(UserCreateDTO user){

        boolean email = validateEmail(user.getEmail());
        boolean phone = validatePhone(user.getPhoneNumber());

        if(email && phone){
            throw new RuntimeException("User with both e-mail and phone already exists");
        }
        if (email){
            throw new RuntimeException("User with this e-mail already exists");
        }
        if (phone){
            throw new RuntimeException("User with this phone already exists");
        }

        validateRole(user.getRole());

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        return userMapper.fromIcesiUser(userRepository.save(icesiUser));
    }

    public boolean validateEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean validatePhone(String phone){
        return userRepository.findByPhone(phone).isPresent();
    }

    public void validateRole(RoleCreateDTO roleName){
        Optional.ofNullable(roleName).orElseThrow(() -> new RuntimeException("User must have a role"));

        roleRepository.findByName(roleName.getName()).orElseThrow(() -> new RuntimeException("Role does not exist"));
    }


}
