package com.example.demo.Mapper;

import com.example.demo.DTO.IcesiRoleDTO;
import com.example.demo.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {
    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);

    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO icesiRoleDTO);
}
