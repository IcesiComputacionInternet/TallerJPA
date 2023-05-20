package co.com.icesi.icesiAccountSystem.service;


import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public ResponseUserDTO saveUser(RequestUserDTO requestUserDTO) {

        Optional<IcesiUser> userByEmail= userRepository.findByEmail(requestUserDTO.getEmail());
        Optional<IcesiUser> userByPhone=userRepository.findByPhoneNumber(requestUserDTO.getPhoneNumber());
        var role = roleRepository.findByName(requestUserDTO.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role was not specified or does not exist yet."));

        if (userByEmail.isPresent() && userByPhone.isPresent()){
            throw new RuntimeException("A User with the same email and phone already exists.");
        }
        if (userByEmail.isPresent()){
            throw new RuntimeException("A User with the same email already exists.");
        }
        if(userByPhone.isPresent()){
            throw new RuntimeException("A User with the same phone already exists.");
        }

        IcesiUser icesiUser = userMapper.fromUserDTO(requestUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(role);
        userRepository.save(icesiUser);
        return userMapper.fromUserToResponseUserDTO(icesiUser);
    }

    public ResponseUserDTO getUser(String userEmail) {
        Optional<IcesiUser> userByEmail=userRepository.findByEmail(userEmail);
        if (!userByEmail.isPresent()){

            throw new RuntimeException("The user with the specified email does not exists.");
        }
        return userMapper.fromUserToResponseUserDTO(userByEmail.get());
    }

    public List<ResponseUserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::fromUserToResponseUserDTO).collect(Collectors.toList());
    }
}
