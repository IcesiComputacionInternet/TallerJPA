package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.exception.UserExistsException;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper icesiUserMapper;

    @SneakyThrows
    public void save(UserDTO userDTO){

        boolean emailFlag = userRepository.existsByEmail(userDTO.getEmail());
        boolean phoneFlag = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());

        if(emailFlag && phoneFlag) throw new UserExistsException("Email and Phone is already used");
        if (emailFlag) throw new UserExistsException("Email already exists");
        if (phoneFlag) throw new UserExistsException("Phone number already exists");

        userDTO.setUserId(UUID.randomUUID());
        userRepository.save(icesiUserMapper.fromUserDTO(userDTO));

    }
}
