package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "icesiRole", source = "icesiRole",ignore=true)
    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);

    @Mapping(target = "icesiRole", source = "icesiRole",ignore=true)
    UserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}
