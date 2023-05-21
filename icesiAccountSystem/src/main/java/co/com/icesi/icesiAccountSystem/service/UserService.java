package co.com.icesi.icesiAccountSystem.service;


import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import co.com.icesi.icesiAccountSystem.enums.ErrorCode;
import co.com.icesi.icesiAccountSystem.error.exception.DetailBuilder;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder.createAccountSystemException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public ResponseUserDTO saveUser(RequestUserDTO requestUserDTO, String type_user) {
        List<DetailBuilder> errors = new ArrayList<>();
        if(type_user.equals("bank_user") && requestUserDTO.getRoleName().equals("ADMIN")){
            throw createAccountSystemException(
                    "A bank user can't create users of type ADMIN.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_400, "role status is", requestUserDTO.getRoleName())
            ).get();
        }
        var role = roleRepository.findByName(requestUserDTO.getRoleName());
        if(!role.isPresent()){
            errors.add(new DetailBuilder(ErrorCode.ERR_404, "Role", "name", requestUserDTO.getRoleName()));
        }
        validateIfEmailIsAlreadyRegistered(requestUserDTO.getEmail(),errors);
        validateIfPhoneIsAlreadyRegistered(requestUserDTO.getPhoneNumber(),errors);

        if (!errors.isEmpty()){
            throw createAccountSystemException(
                    "Some fields of the new user had errors",
                    HttpStatus.BAD_REQUEST,
                    errors.stream().toArray(DetailBuilder[]::new)
            ).get();
        }

        IcesiUser icesiUser = userMapper.fromUserDTO(requestUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(role.get());
        userRepository.save(icesiUser);
        return userMapper.fromUserToResponseUserDTO(icesiUser);
    }

    private void validateIfEmailIsAlreadyRegistered(String userEmail, List<DetailBuilder> errors){
        if(userRepository.findByEmail(userEmail).isPresent()){
            errors.add(new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "email", userEmail));
        }
    }

    private void validateIfPhoneIsAlreadyRegistered(String userPhone, List<DetailBuilder> errors){
        if(userRepository.findByPhoneNumber(userPhone).isPresent()){
            errors.add(new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "phone", userPhone));
        }
    }

    public ResponseUserDTO getUser(String userEmail) {
        var userByEmail=userRepository.findByEmail(userEmail)
                .orElseThrow(
                        createAccountSystemException(
                                "The user with the specified email does not exists.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "User", "email", userEmail)
                        )
                );
        return userMapper.fromUserToResponseUserDTO(userByEmail);
    }

    public List<ResponseUserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::fromUserToResponseUserDTO).collect(Collectors.toList());
    }

}
