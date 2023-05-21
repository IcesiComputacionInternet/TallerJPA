package com.example.demo.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.ResponseIcesiRoleDTO;
import com.example.demo.error.exception.DetailBuilder;
import com.example.demo.error.exception.ErrorCode;
import com.example.demo.error.util.IcesiExceptionBuilder;
import com.example.demo.mapper.IcesiRoleMapper;
import com.example.demo.model.IcesiRole;
import com.example.demo.repository.IcesiRoleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IcesiRoleService {
    
    private final IcesiRoleRepository icesiRoleRepository;
    private final IcesiRoleMapper icesiRoleMapper;

    public ResponseIcesiRoleDTO create(IcesiRoleCreateDTO icesiRoleCreateDTO) {

        Optional<IcesiRole> existingIcesiRole = icesiRoleRepository.findByName(icesiRoleCreateDTO.getName());

        existingIcesiRole.ifPresent(u -> {
            throw IcesiExceptionBuilder.createIcesiException(
                "This role name is already in use",
                HttpStatus.BAD_REQUEST,
                new DetailBuilder(ErrorCode.ERR_400, "role", "name", icesiRoleCreateDTO.getName())
            ).get();
        });

        IcesiRole newIcesiRole = icesiRoleMapper.fromIcesiRoleCreateDTO(icesiRoleCreateDTO);
        newIcesiRole.setRoleId(UUID.randomUUID());
        
        return icesiRoleMapper.fromIcesiRoleToResponseIcesiRoleDTO(icesiRoleRepository.save(newIcesiRole));
    }
}
