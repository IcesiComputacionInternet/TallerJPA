package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.Icesi.TallerJPA.error.exception.IcesiException;
import com.Icesi.TallerJPA.mapper.IcesiUserMapper;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import com.Icesi.TallerJPA.repository.IcesiUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class IcesiUserService {
    private final IcesiUserRepository icesiUserRepository;
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiUserMapper icesiUserMapper;



    public IcesiUserDTO save(IcesiUserDTO icesiUserDTO) {
        boolean foundPhone = icesiUserRepository.finByPhoneNumber(icesiUserDTO.getPhoneNumber()).isPresent();
        boolean foundEmail = icesiUserRepository.findByEmail(icesiUserDTO.getEmail()).isPresent();
        if (foundEmail && foundPhone) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_03.getMessage()));

        }
        if (foundEmail) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_01.getMessage()));
        }
        if (foundPhone) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_02.getMessage()));

        }
        if (icesiUserDTO.getIcesiRole() == null) {
            throw new RuntimeException(String.valueOf(ErrorConstants.CODE_UD_04.getMessage()));
        }
        IcesiRole icesiRoleDTO=  icesiRoleRepository.findByName(icesiUserDTO.getIcesiRole()).orElseThrow(() -> new RuntimeException("The role "+icesiUserDTO.getIcesiRole()+" not exists"));

        IcesiUser icesiUser = icesiUserMapper.fromIcesiUserDTO(icesiUserDTO);
        icesiUser.setIcesiRole(icesiRoleDTO);
        icesiUser.setUserId(UUID.randomUUID());
        return icesiUserMapper.fromIcesiUser(icesiUserRepository.save(icesiUser)) ;
    }


    public void foundRoleName(IcesiUser icesiUser) throws IcesiException {
        if (icesiRoleRepository.findExistName(icesiUser.getIcesiRole().getName())) {
            throw new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_05, ErrorConstants.CODE_UD_05.getMessage()));
        }
    }

    public IcesiUser getUserByEmail(String userEmail) {
        return icesiUserRepository.findByEmail(userEmail).orElseThrow(() -> {
            throw new IcesiException(HttpStatus.BAD_REQUEST, new IcesiError(ErrorConstants.CODE_UD_15, ErrorConstants.CODE_UD_15.getMessage()));
        });
    }

    public List<IcesiUser> getUsers() {
        return new ArrayList<>(icesiUserRepository.findAll());
    }
}
