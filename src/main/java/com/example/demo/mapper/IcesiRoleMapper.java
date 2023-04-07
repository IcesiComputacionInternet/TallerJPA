package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.model.IcesiRole;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    
    IcesiRole fromIcesiRoleCreateDTO(IcesiRoleCreateDTO icesiRoleCreateDTO);
    IcesiRoleCreateDTO fromIcesiRole(IcesiRole icesiRole);
}
