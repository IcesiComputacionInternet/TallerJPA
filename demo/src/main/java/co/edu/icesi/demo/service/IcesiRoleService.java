package co.edu.icesi.demo.service;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.error.DetailBuilder;
import co.edu.icesi.demo.error.ErrorCode;
import co.edu.icesi.demo.mapper.IcesiRoleMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.UUID;

import static co.edu.icesi.demo.error.IcesiExceptionBuilder.createIcesiException;

@Service
@AllArgsConstructor
@Builder
public class IcesiRoleService {

    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;

    public IcesiRole saveRole(IcesiRoleDto roleToSave){

        //Role name should be unique
        icesiRoleRepository.findByName(roleToSave.getName()).orElseThrow(
                createIcesiException(
                        "Role already exists",
                        HttpStatus.BAD_REQUEST,
                        new DetailBuilder(ErrorCode.ERR_DUPLICATED, "Role","Name", roleToSave.getName())
                ));

            //comvert from dto to actual role
            IcesiRole icesiRole = icesiRoleMapper.fromIcesiRoleDto(roleToSave);
            //Generate id
            icesiRole.setRoleId(UUID.randomUUID());
            return icesiRoleRepository.save(icesiRole);

    }
}
