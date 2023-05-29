package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.config.PasswordEncoderConfiguration;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.dto.responseDTO.IcesiUserCreateResponseDTO;
import co.com.icesi.TallerJPA.error.exception.DetailBuilder;
import co.com.icesi.TallerJPA.error.exception.ErrorCode;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.security.IcesiSecurityContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.UUID;

import static co.com.icesi.TallerJPA.error.util.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository userRepository;
    private final IcesiRoleRepository roleRepository;
    private final IcesiUserMapper userMapper;
    private final PasswordEncoderConfiguration passwordEncoderConfiguration;

    public IcesiUserCreateResponseDTO save(IcesiUserCreateDTO userDTO){
        IcesiRole role  = findRole(userDTO);
        rolePermissions(userDTO);
        UserPhoneAndEmailValidation(userDTO);


        IcesiUser user = userMapper.fromIcesiUserDTO(userDTO);
        user.setPassword(passwordEncoderConfiguration.passwordEncoder().encode(user.getPassword()));
        user.setUserId(UUID.randomUUID());
        user.setRole(role);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    private IcesiRole findRole(IcesiUserCreateDTO userDTO) {
        return roleRepository.findByName(userDTO.getRole().getName()).orElseThrow(
                createIcesiException(
                        "This role doesn't exist in the database: "+userDTO.getRole(),
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "User role",userDTO.getRole().getName())));
    }

    private void rolePermissions(IcesiUserCreateDTO userDTO){
        String role = IcesiSecurityContext.getCurrentUserRole();
        if (role.equalsIgnoreCase("Admin")) {
            userAdmin(role);
        }
        else if (role.equalsIgnoreCase("Bank")) {
            userBank(role,userDTO);
        }
    }

    private void userAdmin(String userRole){
        if (userRole.equalsIgnoreCase("USER")){
            throw createIcesiException(
                    "This user does not have authorization",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    private void userBank(String userRole, IcesiUserCreateDTO userDTO){
        if (userRole.equalsIgnoreCase("BANK") && userDTO.getRole().getName().equalsIgnoreCase("ADMIN")){
            throw createIcesiException(
                    "This user does not have authorization",
                    HttpStatus.UNAUTHORIZED,
                    new DetailBuilder(ErrorCode.ERR_401)
            ).get();
        }
    }

    private void UserPhoneAndEmailValidation(IcesiUserCreateDTO userDTO){
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent() && userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()){
            throw createIcesiException(
                    "This user email "+userDTO.getEmail()+" and this phone number "+userDTO.getPhoneNumber()+" already exists in the database",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Email","Phone number")).get();
        }

        else if (userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw createIcesiException(
                    "This user email "+userDTO.getEmail()+" already exists in the database",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Email",userDTO.getEmail() )).get();
        }

        else if(userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()){
            throw createIcesiException(
                    "This user phone number "+userDTO.getPhoneNumber()+" already exists in the database",
                    HttpStatus.NOT_ACCEPTABLE,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Phone Number",userDTO.getPhoneNumber())).get();
        }
    }

    public IcesiUser getUserById() {
        UUID id= UUID.fromString(IcesiSecurityContext.getCurrentUserId());
        IcesiUser user = userRepository.findById(id).orElseThrow(
                createIcesiException(
                        "User not found with ID: " + IcesiSecurityContext.getCurrentUserId(),
                        HttpStatus.NOT_FOUND,
                        new DetailBuilder(ErrorCode.ERR_404, "User", "userID", IcesiSecurityContext.getCurrentUserId())
                )
        );
        return userRepository.findById(id).get();
    }
}

