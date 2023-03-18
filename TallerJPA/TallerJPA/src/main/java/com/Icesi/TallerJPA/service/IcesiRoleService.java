package com.Icesi.TallerJPA.service;

import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.enums.ErrorConstants;
import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.Icesi.TallerJPA.error.exception.IcesiException;
import com.Icesi.TallerJPA.mapper.IcesiRoleMapper;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    private final IcesiRoleRepository icesiRoleRepository;

    private final IcesiRoleMapper icesiRoleMapper;


    public String saveRole(IcesiRoleDTO icesiRoleDTO) {

        IcesiRole icesiRole = icesiRoleMapper.fromIcesiRoleDTO(icesiRoleDTO);
        nameRole(icesiRole);

        icesiRole.setRoleId(UUID.randomUUID());
        icesiRoleRepository.save(icesiRole);
        return "Role saved";

    }
    public IcesiRole save(IcesiRoleDTO roleCreateDTO){

        if(icesiRoleRepository.findExistName(roleCreateDTO.getName())){
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_05.getMessage()));
        }

        IcesiRole icesiRole = icesiRoleMapper.fromIcesiRoleDTO(roleCreateDTO);
        icesiRole.setRoleId(UUID.randomUUID());

        return icesiRoleRepository.save(icesiRole);
    }
    private void nameRole(IcesiRole icesiRole) {
        boolean foundName = icesiRoleRepository.findExistName(icesiRole.getName());
        if (foundName)
            throw new RuntimeException(String.valueOf( ErrorConstants.CODE_UD_05.getMessage()));
    }


}
