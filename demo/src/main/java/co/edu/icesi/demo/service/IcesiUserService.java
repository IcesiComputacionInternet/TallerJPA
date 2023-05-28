package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.error.DetailBuilder;
import co.edu.icesi.demo.error.ErrorCode;
import co.edu.icesi.demo.mapper.IcesiUserMapper;

import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static co.edu.icesi.demo.error.IcesiExceptionBuilder.createIcesiException;


@Service
@AllArgsConstructor
@Builder
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;

    private final IcesiRoleRepository icesiRoleRepository;


    public IcesiUserDto save(IcesiUserDto user){


        validatePhoneAndEmail(user.getPhoneNumber(), user.getEmail());
        validateRole(user.getRole());

        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDto(user);
        icesiUser.setUserId(UUID.randomUUID());

        return icesiUserMapper.fromIcesiUser(icesiUserRepository.save(icesiUser));
    }

    //
    public boolean validateEmail(String email){
        return icesiUserRepository.findByEmail(email).isPresent();
    }

    public boolean validatePhone(String phone){
        return icesiUserRepository.findByPhoneNumber(phone).isPresent();
    }

    //
    public void validatePhoneAndEmail(String phoneNumber, String mail){
        boolean email = validateEmail(mail);
        boolean phone = validatePhone(phoneNumber);

        if(email && phone){
            throw createIcesiException(
                    "User already has both email and phone",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "E-mail", mail),
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "Phone", phoneNumber)
            ).get();}
        if (email){
            throw createIcesiException(
                    "User with this e-mail already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "E-mail", mail)
            ).get();}
        if (phone){
            throw createIcesiException(
                    "User with this phone already exists",
                    HttpStatus.BAD_REQUEST,
                    new DetailBuilder(ErrorCode.ERR_DUPLICATED, "User", "Phone", phoneNumber)
            ).get();}
    }

    /*
    public void validatePhoneEmail(IcesiUserDto icesiUserToSave){
        if(icesiUserRepository.findByEmail(icesiUserToSave.getEmail()).isPresent()){
            if(icesiUserRepository.findByPhoneNumber(icesiUserToSave.getPhoneNumber()).isPresent()){
                throw new RuntimeException("Both email and phone number are already taken");
            }else{
                throw new RuntimeException("Email has already been taken");
            }
        }
        if(icesiUserRepository.findByPhoneNumber(icesiUserToSave.getPhoneNumber()).isPresent()){
            throw new RuntimeException("Phone number has already been taken");
        }
    }*/

    public void validateRole(IcesiRoleDto roleName){
        Optional.ofNullable(roleName).orElseThrow(createIcesiException(
                "User must have a role",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_REQUIRED_FIELD, "Role", "Name")
        ));


        icesiRoleRepository.findByRoleName(roleName.getName()).orElseThrow(createIcesiException(
                "Role does not exist",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_404, "Role", "Name", roleName.getName())
        ));
    }
}
