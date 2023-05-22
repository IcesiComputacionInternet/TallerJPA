package com.example.demo.service;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.error.exception.DetailBuilder;
import com.example.demo.error.exception.ErrorCode;
import com.example.demo.error.util.IcesiExceptionBuilder;
import com.example.demo.mapper.IcesiUserMapper;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.model.enums.TypeIcesiRole;
import com.example.demo.repository.IcesiRoleRepository;
import com.example.demo.repository.IcesiUserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor 
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;
    private final IcesiRoleRepository icesiRoleRepository;

    public  ResponseIcesiUserDTO create(String userCreatorRole, IcesiUserCreateDTO user) {

        validateAdminRole(userCreatorRole, user.getIcesiRoleCreateDTO().getName());

        Optional<IcesiUser> userWithEmail = icesiUserRepository.findByEmail(user.getEmail());
        Optional<IcesiUser> userWithPhone = icesiUserRepository.findByPhoneNumber(user.getPhoneNumber());
        
        if (userWithEmail.isPresent() && userWithPhone.isPresent()) {
            throw IcesiExceptionBuilder.createIcesiException(
                "These email and phone number are already in use",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "email and phone number", user.getEmail() + " " + user.getPhoneNumber())
            ).get();
        }

        userWithEmail.ifPresent(u -> {
            throw IcesiExceptionBuilder.createIcesiException(
                "This email is already in use",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "email", user.getEmail())
            ).get();
        });

        userWithPhone.ifPresent(u -> {
            throw IcesiExceptionBuilder.createIcesiException(
                "This phone number is already in use",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "phone number", user.getPhoneNumber())
            ).get();
        });
        
        IcesiRole icesiRole = icesiRoleRepository.findByName(user.getIcesiRoleCreateDTO().getName())
            .orElseThrow(() -> IcesiExceptionBuilder.createIcesiException(
                "This role is not present in the database",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "role", "name", user.getIcesiRoleCreateDTO().getName())
            ).get());
        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserCreateDTO(user);

        icesiUser.setIcesiRole(icesiRole);
        icesiUser.setUserId(UUID.randomUUID());

        return icesiUserMapper.fromIcesiUserToResponseIcesiUserDTO(icesiUserRepository.save(icesiUser));

    }

    //Param userModifier corresponds to the role of the user who is modifying the role of another user
    public ResponseIcesiUserDTO updateRole(String userModifier, IcesiUserCreateDTO userToUpdateRole, String newRole) {
        //This method validates that the user who is modifying the role of another user has the admin role
        validateAdminRole(userModifier, newRole);

        IcesiUser user = findIcesiUserByEmail(userToUpdateRole.getEmail());
        IcesiRole role = findIcesiRoleByName(newRole);
    
        user.setIcesiRole(role);

        return icesiUserMapper.fromIcesiUserToResponseIcesiUserDTO(icesiUserRepository.save(user));
    }

    public IcesiUser findIcesiUserByEmail(String email) {
        return icesiUserRepository.findByEmail(email)
            .orElseThrow(() -> IcesiExceptionBuilder.createIcesiException(
                "This email is not present in the database",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "email", "not present", email)
            ).get());
    }

    public IcesiRole findIcesiRoleByName(String name) {
        return icesiRoleRepository.findByName(name)
            .orElseThrow(() -> IcesiExceptionBuilder.createIcesiException(
                "This role is not present in the database",
                HttpStatus.NOT_FOUND,
                new DetailBuilder(ErrorCode.ERR_404, "role", "name", name)
            ).get());
    }


    /*This method receives two params:
    *the first one is the user who is modifying the role of another user
    *The second one is the role that the user is trying to assign to another user or add to the list of roles*/
    private void validateAdminRole(String userModifier, String role) {
        if (!userModifier.equals(TypeIcesiRole.admin.name()) && role.equals(TypeIcesiRole.admin.name())) {
            throw IcesiExceptionBuilder.createIcesiException(
                "Forbidden",
                HttpStatus.FORBIDDEN,
                new DetailBuilder(ErrorCode.ERR_403, "Only a user with admin role can assign the admin role to another user")
            ).get();
        }
    }

}
