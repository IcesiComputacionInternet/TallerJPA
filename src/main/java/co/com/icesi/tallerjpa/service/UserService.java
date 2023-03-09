package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.exception.UserExistsException;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper icesiUserMapper;

    @SneakyThrows
    public IcesiUser save(UserDTO userDTO){

        boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
        boolean phoneExists = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());

        if (emailExists && phoneExists){ throw new UserExistsException("Email and Phone is already used");}
        if (emailExists){ throw new UserExistsException("Email already exists");}
        if (phoneExists){ throw new UserExistsException("Phone number already exists");}

        userDTO.setUserId(UUID.randomUUID());
        return userRepository.save(icesiUserMapper.fromUserDTO(userDTO));

    }
}
