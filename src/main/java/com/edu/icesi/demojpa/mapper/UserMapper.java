package com.edu.icesi.demojpa.mapper;

import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.dto.response.ResponseUserDTO;
import com.edu.icesi.demojpa.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role", ignore = true)
    IcesiUser fromIcesiUserDTO(RequestUserDTO requestUserDTO);

    @Mapping(target = "role", expression = "java(icesiUser.getRole().getName())")
    ResponseUserDTO fromIcesiUser(IcesiUser icesiUser);
}
