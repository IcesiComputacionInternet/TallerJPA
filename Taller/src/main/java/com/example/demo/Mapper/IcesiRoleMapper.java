package com.example.demo.Mapper;

import com.example.demo.DTO.IcesiRoleDTO;
import com.example.demo.model.IcesiRole;

public interface IcesiRoleMapper {
    IcesiRoleDTO fromIcesiRole(IcesiRole icesiRole);
    IcesiRole fromIcesiRoleDTO(IcesiRoleDTO icesiRoleDTO);
}
