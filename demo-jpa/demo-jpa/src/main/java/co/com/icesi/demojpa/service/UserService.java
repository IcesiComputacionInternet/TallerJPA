package co.com.icesi.demojpa.service;

import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.UserResponseDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import co.com.icesi.demojpa.repository.UserRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;

    public UserResponseDTO save(UserCreateDTO user){

        if(userRepository.existsByEmail(user.getEmail()) && userRepository.existsByPhone(user.getPhone())){
            throw new RuntimeException("User phone and email are already in use");
        }else if(userRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("User email is already in use");
        } else if (userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("User phone is already in use");
        }


        IcesiUser icesiUser = userMapper.fromUserCreateDTO(user);
        icesiUser.setUserId(UUID.randomUUID());

        if(roleRepository.existsByName(user.getRoleName())){
            icesiUser.setRole(roleRepository.findByName(user.getRoleName()).get());
        }else{
            throw new RuntimeException("Role doesn't exists");
        }

        return userMapper.fromUserToSendUserDTO(userRepository.save(icesiUser));
    }
}
