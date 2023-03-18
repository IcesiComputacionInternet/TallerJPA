package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.DTO.IcesiUserCreateDTO;
import com.example.demo.model.IcesiUser;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    
    IcesiUser fromIcesiUserDTO(IcesiUserCreateDTO userCreateDTO);

    IcesiUserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}