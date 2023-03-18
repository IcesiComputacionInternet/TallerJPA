package com.example.demo.Mapper;

import com.example.demo.DTO.IcesiUserDTO;
import com.example.demo.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {

    @Mapping(source = "icesiRole.roleId", target = "icesiRoleId")
    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);
    @Mapping(target = "icesiRole", ignore = true)
    IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO);
}
