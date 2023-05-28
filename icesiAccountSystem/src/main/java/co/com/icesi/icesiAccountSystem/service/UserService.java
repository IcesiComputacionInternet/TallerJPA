package co.com.icesi.icesiAccountSystem.service;


import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import co.com.icesi.icesiAccountSystem.enums.ErrorCode;
import co.com.icesi.icesiAccountSystem.error.exception.DetailBuilder;
import co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder;
import co.com.icesi.icesiAccountSystem.mapper.RoleMapper;
import co.com.icesi.icesiAccountSystem.mapper.UserMapper;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.model.IcesiRole;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.RoleRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import co.com.icesi.icesiAccountSystem.controller.HomeController;
import co.com.icesi.icesiAccountSystem.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder.createAccountSystemError;
import static co.com.icesi.icesiAccountSystem.error.util.AccountSystemExceptionBuilder.createAccountSystemException;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public ResponseUserDTO saveUser(RequestUserDTO requestUserDTO) {
        IcesiRole role = roleRepository.findByName(requestUserDTO.getRole()).orElseThrow(
                createAccountSystemException(
                                "Role does not exist.",
                                HttpStatus.NOT_FOUND,
                                new DetailBuilder(ErrorCode.ERR_404, "Role", "name", requestUserDTO.getRole())
                )
        );
        validateIfEmailIsDuplicated(requestUserDTO.getEmail());
        validateIfPhoneIsDuplicated(requestUserDTO.getPhoneNumber());

        checkPermissions(role.getName());
        IcesiUser icesiUser = userMapper.fromUserDTO(requestUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(role);
        userRepository.save(icesiUser);
        ResponseUserDTO responseUserDTO = userMapper.fromUserToResponseUserDTO(icesiUser);
        responseUserDTO.setRole(roleMapper.fromRoleToRoleDTO(role));
        return responseUserDTO;
    }

    private void checkPermissions(String roleToAssign) {
        if((roleToAssign.equals("ADMIN") && IcesiSecurityContext.getCurrentUserRole().equals("BANK_USER"))||(IcesiSecurityContext.getCurrentUserRole().equals("USER"))){
            throw createAccountSystemException(
                    "A normal user or a bank user can't create users of type ADMIN.",
                    HttpStatus.FORBIDDEN,
                    new DetailBuilder(ErrorCode.ERR_403)
            ).get();
        }
    }

    private void validateIfEmailIsDuplicated(String userEmail){
        if(userRepository.findByEmail(userEmail).isPresent()){
            throw createAccountSystemException(
                    "A user with the entered email already exists.",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "email", userEmail)
            ).get();
        }
    }

    private void validateIfPhoneIsDuplicated(String userPhone){
        if(userRepository.findByPhoneNumber(userPhone).isPresent()){
            throw createAccountSystemException(
                    "A user with the entered phone already exists.",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "phone", userPhone)
            ).get();
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
