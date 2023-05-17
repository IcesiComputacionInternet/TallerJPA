package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.mapper.IcesiUserMapper;

import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
@Builder
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;

    private final IcesiRoleRepository icesiRoleRepository;


    public IcesiUserDto saveUser(IcesiUserDto icesiUserToSave){
        validatePhoneEmail(icesiUserToSave);
        validateRole(icesiUserToSave.getRole());


        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDto(icesiUserToSave);
        icesiUser.setUserId(UUID.randomUUID());
        return icesiUserMapper.fromIcesiUser(icesiUserRepository.save(icesiUser));
    }

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
    }

    public void validateRole(IcesiRoleDto roleName){
        Optional.ofNullable(roleName).orElseThrow(() -> new RuntimeException("User must have a role"));

        icesiRoleRepository.findByName(roleName.getName()).orElseThrow(() -> new RuntimeException("Role does not exist"));
    }
}
