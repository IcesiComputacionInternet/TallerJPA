package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.mapper.IcesiUserMapper;

import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
@Builder
public class IcesiUserService {

    private final IcesiUserRepository icesiUserRepository;
    private final IcesiUserMapper icesiUserMapper;


    public IcesiUser saveUser(IcesiUserDto icesiUserToSave){
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
        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDto(icesiUserToSave);
        icesiUser.setUserId(UUID.randomUUID());
        return icesiUserRepository.save(icesiUser);
    }
}
