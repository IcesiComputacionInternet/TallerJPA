package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.DTO.IcesiRoleCreateDTO;
import com.example.demo.DTO.ResponseIcesiRoleDTO;
import com.example.demo.model.IcesiRole;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    
    IcesiRole fromIcesiRoleCreateDTO(IcesiRoleCreateDTO icesiRoleCreateDTO);
    ResponseIcesiRoleDTO fromIcesiRoleToResponseIcesiRoleDTO(IcesiRole icesiRole);
}
