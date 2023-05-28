package com.edu.icesi.TallerJPA.service;

import com.edu.icesi.TallerJPA.Enums.Scopes;
import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.error.exception.DetailBuilder;
import com.edu.icesi.TallerJPA.error.exception.ErrorCode;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.mapper.UserMapper;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiRole;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.RoleRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.edu.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AccountMapper accountMapper;

    private final RoleRepository roleRepository;

    public UserCreateDTO save(UserCreateDTO userCreateDTO){

        definePermissions(userCreateDTO);

        verifyEmailAndPhoneNumber(userCreateDTO.getEmail(), userCreateDTO.getPhoneNumber());

        findByEmailIfIsDuplicated(userCreateDTO.getEmail());
        findByPhoneNumberIfIsDuplicated(userCreateDTO.getPhoneNumber());

        String roleName = userCreateDTO.getIcesiRole();
        IcesiRole role = roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("The role "+roleName+" not exists"));

        IcesiUser icesiUser = userMapper.fromIcesiUserDTO(userCreateDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUser.setIcesiRole(role);
        return userMapper.fromIcesiUser(userRepository.save(icesiUser));
    }

    private void definePermissions(UserCreateDTO userCreateDTO){

        String roleName = userCreateDTO.getIcesiRole();

        switch (roleName) {
            case "ADMIN" -> verifyUserRoleForAdmin(IcesiSecurityContext.getCurrentRol());
            case "BANK" -> verifyUserRoleForBank(IcesiSecurityContext.getCurrentRol());
        }
    }

    public void verifyUserRoleForAdmin(String roleActualUser){

        if (!roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.ADMIN))){
            throw createIcesiException(
                    "User unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    public void verifyUserRoleForBank(String roleActualUser){

        if (roleActualUser.equalsIgnoreCase(String.valueOf(Scopes.USER))){
            throw createIcesiException(
                    "User unauthorized",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    private void verifyEmailAndPhoneNumber(String email, String phoneNumber){

        if (userRepository.findByEmail(email).isPresent() && userRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw createIcesiException(
                    "Invalid values",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_400, "The email and phone number","save a user", "The email and phone number already exists")
            ).get();
        }
    }

    public void findByEmailIfIsDuplicated(String email){

        Optional<IcesiUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw createIcesiException(
                    "Duplicated email",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "email", email)
            ).get();
        }
    }

    public void findByPhoneNumberIfIsDuplicated(String phoneNumber){

        Optional<IcesiUser> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            throw createIcesiException(
                    "Duplicated phoneNumber",
                    HttpStatus.CONFLICT,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "user", "phone number", phoneNumber)
            ).get();
        }
    }

    public UserCreateDTO findByEmail(String email){
        return userMapper.fromIcesiUser(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("The user with email "+email+" not exists")));
    }

    public UserCreateDTO findByPhoneNumber(String phoneNumber){
        return userMapper.fromIcesiUser(userRepository.findByEmail(phoneNumber).orElseThrow(() -> new RuntimeException("The user with phone number "+phoneNumber+" not exists")));
    }

    public List<AccountCreateDTO> getAccounts() {

        IcesiUser user = getUserById(IcesiSecurityContext.getCurrentUserId());

        return saveAccountsOfUser(user.getAccounts());
    }

    private List<AccountCreateDTO> saveAccountsOfUser(List<IcesiAccount> accounts){
        return accounts.stream().map(accountMapper::fromIcesiAccount).toList();
    }

    private IcesiUser getUserById(String userId){

        if (userRepository.findById(UUID.fromString(userId)).isEmpty()){
            throw createIcesiException(
                    "User not found",
                    HttpStatus.NOT_FOUND,
                    new DetailBuilder(ErrorCode.ERR_404, "User", "id", userId)
            ).get();
        }
        return userRepository.findById(UUID.fromString(userId)).get();
    }

}
