package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.Icesi.TallerJPA.error.exception.IcesiException;
import com.Icesi.TallerJPA.mapper.IcesiUserMapper;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import com.Icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiUserService  {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiUserMapper icesiUserMapper;

    public  String newUser(IcesiUserDTO icesiUserDTO){
        String out = "saved User";

        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDTO(icesiUserDTO);
        save(icesiUserDTO);
        foundRoleName(icesiUser);
        if(icesiUserDTO.getIcesiRole().getName() != null){
        icesiUser.setIcesiRole(icesiUserDTO.getIcesiRole());
        icesiUser.setUserId(UUID.randomUUID());
        icesiUserRepository.save(icesiUser);
        }else{
            throw new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_04, ErrorConstants.CODE_UD_04.getMessage()));
            }
        return out;
    }


    public IcesiUser save(IcesiUserDTO icesiUserDTO) {
        boolean foundPhone = icesiUserRepository.finByPhoneNumber(icesiUserDTO.getPhoneNumber());
        boolean foundEmail = icesiUserRepository.finByEmail(icesiUserDTO.getEmail());
        if(foundEmail && foundPhone){
            throw  new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_03.getMessage()));

        }else if(foundEmail){
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_01.getMessage()));
        }else if(foundPhone){
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_02.getMessage()));

        } else if (icesiUserDTO.getIcesiRole() == null) {
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_04.getMessage()));
        }
        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDTO(icesiUserDTO);
        icesiUser.setUserId(UUID.randomUUID());

        return icesiUserRepository.save(icesiUser);
    }


    public void foundRoleName(IcesiUser icesiUser) throws IcesiException {

        boolean foundNameRole=   icesiRoleRepository.findExistName(icesiUser.getIcesiRole().getName());
        if(foundNameRole){
            throw new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_05, ErrorConstants.CODE_UD_05.getMessage()));
        }


    }



}
