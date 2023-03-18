package com.example.demo.Mapper;

import com.example.demo.DTO.IcesiUserDTO;
import com.example.demo.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);
    IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO);
}
