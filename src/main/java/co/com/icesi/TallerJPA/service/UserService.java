package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.exception.ArgumentsException;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapper;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;


    public UserResponseDTO save(UserCreateDTO user) {
        boolean email = userRepository.findByEmail(user.getEmail());
        boolean phoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if(email && phoneNumber){
            throw new ArgumentsException("Email and phone number already exist");
        }else if (email) {
            throw new ArgumentsException("Email already exist");
        }else if (phoneNumber) {
            throw new ArgumentsException("Phone number already exist");
        }

        boolean existRole = roleRepository.findByName(user.getRole());
        if (!existRole) {
            throw new ArgumentsException("Role does not exist");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setRole(roleRepository.returnRole(user.getRole()).orElseThrow(() -> new RuntimeException("Role not found")));
        icesiUser.setUserId(UUID.randomUUID());
        //userResponseMapper.fromICesiUSer(userRepository.save(userMapper.fromIcesiUserDTO(user)));
        UserResponseDTO userResponseDTO = userResponseMapper.fromICesiUSer(userRepository.save(icesiUser));
        userResponseDTO.setUserId(icesiUser.getUserId());

        return userResponseDTO;
    }
}
