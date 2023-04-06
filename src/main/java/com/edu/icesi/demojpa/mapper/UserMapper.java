package com.edu.icesi.demojpa.mapper;

import com.edu.icesi.demojpa.dto.RequestUserDTO;
import com.edu.icesi.demojpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    IcesiUser fromIcesiUserDTO(RequestUserDTO requestUserDTO);
}
