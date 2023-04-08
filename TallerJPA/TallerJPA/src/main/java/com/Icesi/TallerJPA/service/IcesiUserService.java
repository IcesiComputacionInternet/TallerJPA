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

import java.util.Optional;
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
            Optional<IcesiUserDTO> userDTO = Optional.ofNullable(icesiUserDTO);
            String roleName = userDTO.flatMap(dto -> Optional.ofNullable(dto.getIcesiRole()))
                    .map(role -> role.getName()).orElseThrow(() ->new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_04, ErrorConstants.CODE_UD_04.getMessage())));

        icesiUser.setIcesiRole(icesiUserDTO.getIcesiRole());
        icesiUser.setUserId(UUID.randomUUID());
        icesiUserRepository.save(icesiUser);

        return out;
    }


    public IcesiUserDTO save(IcesiUserDTO icesiUserDTO) {

        boolean foundPhone = icesiUserRepository.finByPhoneNumber(icesiUserDTO.getPhoneNumber()).isPresent();
        boolean foundEmail = icesiUserRepository.finByEmail(icesiUserDTO.getEmail()).isPresent();
        if(foundEmail && foundPhone){
            throw  new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_03.getMessage()));

        }
        if(foundEmail){
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_01.getMessage()));
        }
        if(foundPhone){
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_02.getMessage()));

        }
        if (icesiUserDTO.getIcesiRole() == null) {
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_04.getMessage()));
        }
        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDTO(icesiUserDTO);
        icesiUser.setUserId(UUID.randomUUID());
        icesiUserRepository.save(icesiUser);
        return icesiUserMapper.fromIcesiUser(icesiUser);
    }


    public void foundRoleName(IcesiUser icesiUser) throws IcesiException {
        if(icesiRoleRepository.findExistName(icesiUser.getIcesiRole().getName())){
            throw new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_05, ErrorConstants.CODE_UD_05.getMessage()));
        }


    }



}
