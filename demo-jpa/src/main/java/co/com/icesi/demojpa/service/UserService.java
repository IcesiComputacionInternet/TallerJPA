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

    public IcesiUser save(UserCreateDTO user){

        validateEmailAndPhone(user);
        validateRole(user.getRole());

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        return userRepository.save(icesiUser);
    }

    public void validateEmailAndPhone(UserCreateDTO user){
        if(validatePhone(user.getPhoneNumber()) && validateEmail(user.getEmail())){
            throw new RuntimeException("User with both e-mail and phone already exists");
        }
    }

    public boolean validateEmail(String email){
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new RuntimeException("User with this e-mail already exists");
        });
        return true;
    }

    public boolean validatePhone(String phone){
        userRepository.findByPhone(phone).ifPresent(user -> {
            throw new RuntimeException("User with this phone number already exists");
        });
        return true;
    }

    public void validateRole(RoleCreateDTO roleName){
        Optional.ofNullable(roleName).orElseThrow(() -> new RuntimeException("User must have a role"));

        roleRepository.findByName(roleName.getName()).orElseThrow(() -> new RuntimeException("Role does not exist"));
    }


}
