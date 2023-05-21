package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.DTO.ResponseIcesiUserDTO;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    
    IcesiUser fromIcesiUserCreateDTO(IcesiUserCreateDTO icesiUserCreateDTO);
    IcesiUserCreateDTO fromIcesiUserToIUserCreateDTO(IcesiUser icesiUser);
    ResponseIcesiUserDTO fromIcesiUserToResponseIcesiUserDTO(IcesiUser icesiUser);
    IcesiRole fromResponseIcesiUserDTO(ResponseIcesiUserDTO responseIcesiUserDTO);
}