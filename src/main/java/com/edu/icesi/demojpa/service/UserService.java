package com.edu.icesi.demojpa.service;

import com.edu.icesi.demojpa.Security.IcesiSecurityContext;
import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.dto.response.ResponseUserDTO;
import com.edu.icesi.demojpa.error.exception.IcesiException;
import com.edu.icesi.demojpa.error.util.IcesiExceptionBuilder;
import com.edu.icesi.demojpa.mapper.UserMapper;
import com.edu.icesi.demojpa.model.IcesiAccount;
import com.edu.icesi.demojpa.model.IcesiRole;
import com.edu.icesi.demojpa.model.IcesiUser;
import com.edu.icesi.demojpa.repository.RoleRepository;
import com.edu.icesi.demojpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final IcesiExceptionBuilder icesiExceptionBuilder = new IcesiExceptionBuilder();

    public ResponseUserDTO save(RequestUserDTO user){
        hasPermission(user.getRole());
        boolean emailInUse = userRepository.isEmailInUse(user.getEmail()).isPresent();
        boolean phoneNumberInUse = userRepository.isPhoneNumberInUse(user.getPhoneNumber()).isPresent();
        IcesiRole role = roleRepository.findRoleByName(user.getRole()).orElseThrow(() -> new RuntimeException("The role "+ user.getRole() +" doesn't exist"));

        List<IcesiException> errors = new ArrayList<>();

        if(emailInUse){
            errors.add(icesiExceptionBuilder.duplicatedValueException("The email is already in use", user.getEmail()));
        }

        if(phoneNumberInUse){
            errors.add(icesiExceptionBuilder.duplicatedValueException("The phone-number is already in use", user.getPhoneNumber()));
        }

        if (!errors.isEmpty()){
            errors.stream().map(IcesiException::getMessage).forEach(System.out::println);
        }

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(user);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setRole(role);
        userRepository.save(icesiUser);
        return userMapper.fromIcesiUser(icesiUser);
    }

    private void hasPermission(String currentRole) {
        String accountMakerRole = IcesiSecurityContext.getCurrentRole();

        if(accountMakerRole.equalsIgnoreCase("BANK") && currentRole.equalsIgnoreCase("ADMIN")){
            throw icesiExceptionBuilder.noPermissionException("No permission to do that");
        }
    }

    public ResponseUserDTO getUser(String userEmail){
        return userMapper.fromIcesiUser(
                userRepository.isEmailInUse(userEmail)
                        .orElseThrow(() -> icesiExceptionBuilder.notFoundException("The user with email " + userEmail + "doesn't exists", userEmail)));
    }

    public List<ResponseUserDTO> getAllUsers(){
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::fromIcesiUser)
                .collect(Collectors.toList());
    }
}
