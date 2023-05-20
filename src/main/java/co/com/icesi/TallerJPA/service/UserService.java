package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.config.PasswordEncoderConfiguration;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.error.util.DetailBuilder;
import co.com.icesi.TallerJPA.error.enums.ErrorCode;
import co.com.icesi.TallerJPA.error.util.ArgumentsExceptionBuilder;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.mapper.responseMapper.UserResponseMapper;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.RoleRepository;
import co.com.icesi.TallerJPA.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserResponseMapper userResponseMapper;
    private final PasswordEncoderConfiguration passwordEncoderConfiguration;


    public UserResponseDTO save(UserCreateDTO user) {
        boolean email = userRepository.findByEmail(user.getEmail());
        boolean phoneNumber = userRepository.findByPhoneNumber(user.getPhoneNumber());

        if(email && phoneNumber){

            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Existing data",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_403,"Email","Phone number")
            );
            //throw new ArgumentsException("Email and phone number already exist",e);
        }else if (email) {
            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Existing data",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_406,"Email")
            );
            //throw new ArgumentsException("Email already exist");
        }else if (phoneNumber) {
            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Existing data",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_406,"Phone number")
            );
            //throw new ArgumentsException("Phone number already exist");
        }

        boolean existRole = roleRepository.findByName(user.getRole());
        if (!existRole) {

            throw ArgumentsExceptionBuilder.createArgumentsException(
                    "Not existing data",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_NOT_EXITS,"role")
            );
            //throw new ArgumentsException("Role does not exist");
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setRole(roleRepository.returnRole(user.getRole()).orElseThrow(
                //throw new ArgumentsException("Role not found");
                ArgumentsExceptionBuilder.createArgumentsExceptionSup(
                        "Not existing data",
                        HttpStatus.BAD_REQUEST,
                        new DetailBuilder(ErrorCode.ERR_NOT_FOUND,"role")
                )
        ));

        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setPassword(passwordEncoderConfiguration.passwordEncoder().encode(user.getPassword()));
        //userResponseMapper.fromICesiUSer(userRepository.save(userMapper.fromIcesiUserDTO(user)));
        UserResponseDTO userResponseDTO = userResponseMapper.fromICesiUSer(userRepository.save(icesiUser));
        userResponseDTO.setUserId(icesiUser.getUserId());

        return userResponseDTO;
    }

    public UserResponseDTO getUserByEmail(String userEmail) {
        return userResponseMapper.fromICesiUSer(userRepository.findUserByEmail(userEmail).orElseThrow(
                //() -> new RuntimeException("User not found")
                ArgumentsExceptionBuilder.createArgumentsExceptionSup(
                        "Not existing data",
                        HttpStatus.BAD_REQUEST,
                        new DetailBuilder(ErrorCode.ERR_NOT_FOUND,"user")
                )

        ));
    }

    public List<UserResponseDTO> getAllUsers() {
        List<IcesiUser> users = userRepository.findAll();

        return users.stream().map(userResponseMapper::fromICesiUSer).toList();
    }
}
