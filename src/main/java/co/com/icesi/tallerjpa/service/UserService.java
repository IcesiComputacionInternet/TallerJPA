package co.com.icesi.tallerjpa.service;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.error.enums.ErrorCode;
import co.com.icesi.tallerjpa.error.util.DetailBuilder;
import co.com.icesi.tallerjpa.mapper.UserMapper;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.RoleRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static co.com.icesi.tallerjpa.error.util.ExceptionBuilder.createCustomException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @SneakyThrows
    public ResponseUserDTO save(RequestUserDTO userDTO){

        boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
        boolean phoneExists = userRepository.existsByPhoneNumber(userDTO.getPhoneNumber());

        if (emailExists && phoneExists){
            throw createCustomException(
                    "Email and Phone is already used",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_404)
            );
        }

        if (emailExists){
            throw createCustomException(
                    "Email already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_404)
            );
        }

        if (phoneExists){
            throw createCustomException(
                    "Phone number already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_404)
            );
        }

        IcesiUser user = userMapper.fromUserDTO(userDTO);
        user.setUserId(UUID.randomUUID());
        user.setRole(roleRepository.findByName(userDTO.getRole()).orElseThrow(() -> new Exception("Role not found")));

        return userMapper.fromUserToSendUserDTO(userRepository.save(user));

    }
}
