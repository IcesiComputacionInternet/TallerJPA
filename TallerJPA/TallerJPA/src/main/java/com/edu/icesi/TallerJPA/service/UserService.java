package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.Enums.Scopes;
import com.edu.icesi.TallerJPA.dto.IcesiUserDTO;
import com.edu.icesi.TallerJPA.error.exception.DetailBuilder;
import com.edu.icesi.TallerJPA.error.exception.ErrorCode;
import com.edu.icesi.TallerJPA.mapper.RoleMapper;
import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.edu.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    private final RoleRepository roleRepository;

    private final RoleService roleService;

    public IcesiUserDTO save(IcesiUserDTO userCreateDTO){

        definePermissions(userCreateDTO);

        verifyEmailAndPhoneNumber(userCreateDTO.getEmail(), userCreateDTO.getPhoneNumber());

        findByEmailIfIsDuplicated(userCreateDTO.getEmail());
        findByPhoneNumberIfIsDuplicated(userCreateDTO.getPhoneNumber());

        String roleName = userCreateDTO.getIcesiRole();
        IcesiRole role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("The role "+roleName+" not exists"));

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(userCreateDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(role);
        roleService.addUserToRole(roleMapper.fromIcesiRole(role), icesiUser);

        return userMapper.fromIcesiUser(userRepository.save(icesiUser));
    }

    private void definePermissions(IcesiUserDTO userCreateDTO){

        String roleName = userCreateDTO.getIcesiRole();
        verifyRole(roleName);
        switch (roleName) {

            case "BANK" -> verifyUserRoleForBank(IcesiSecurityContext.getCurrentRol());
            case "ADMIN" -> verifyUserRoleForAdmin(IcesiSecurityContext.getCurrentRol());
        }
    }

    private void verifyRole(String role) {
        if (role == null) {

            throw createIcesiException(
                    "ROLE NULL",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    public void verifyUserRoleForAdmin(String roleActualUser){

        if (!roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.ADMIN))){

            throw createIcesiException(
                    "UNAUTHORIZED",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();



        }
    }

    public void verifyUserRoleForBank(String roleActualUser){

        if (roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.USER))){
            throw createIcesiException(
                    "UNAUTHORIZED",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();



        }

    }

    private void verifyEmailAndPhoneNumber(String email, String phoneNumber){

        if (userRepository.findByEmail(email).isPresent() && userRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw createIcesiException(
                    "INVALID VALUES",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "The email and phone number","save a user", "The email and phone number already exists")
            ).get();




        }
    }

    public void findByEmailIfIsDuplicated(String email){

        Optional<IcesiUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {

            throw createIcesiException(
                    "EMAIL DUPLICATED",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "USER", "EMAIL", email)
            ).get();



        }
    }

    public void findByPhoneNumberIfIsDuplicated(String phoneNumber){

        Optional<IcesiUser> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {

            throw createIcesiException(
                    "PHONE NUMBER DUPLICATED",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "USER", "PHONE NUMBER", phoneNumber)
            ).get();


        }
    }

    public IcesiUserDTO findByEmail(String email){
        return userMapper.fromIcesiUser(userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("THE EMAIL "+email+" NOT EXISTS")));
    }

    public IcesiUserDTO findByPhoneNumber(String phoneNumber){
        return userMapper.fromIcesiUser(userRepository.findByEmail(phoneNumber).orElseThrow(() -> new RuntimeException("The user with phone number "+phoneNumber+" not exists")));
    }

}
