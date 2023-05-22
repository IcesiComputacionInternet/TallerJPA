package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.error.exception.CustomException;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @SneakyThrows
    public ResponseUserDTO save(RequestUserDTO userDTO, String role){
        if (role.equalsIgnoreCase("BANK_USER")
                && !userDTO.getRole().equalsIgnoreCase("user")){
            throw new CustomException("Bank users can only create users");
        }

        boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
        boolean phoneExists = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());

        if (emailExists && phoneExists){ throw new CustomException("Email and Phone is already used");}
        if (emailExists){ throw new CustomException("Email already exists");}
        if (phoneExists){ throw new CustomException("Phone number already exists");}

        IcesiUser user = userMapper.fromUserDTO(userDTO);
        user.setUserId(UUID.randomUUID());
        user.setRole(roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new CustomException("Role not found")));

        return userMapper.fromUserToSendUserDTO(userRepository.save(user));

    }
}
