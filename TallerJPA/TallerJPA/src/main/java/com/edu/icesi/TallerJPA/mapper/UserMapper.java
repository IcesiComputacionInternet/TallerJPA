package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.IcesiUserDTO;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "icesiRole", source = "icesiRole",ignore=true)
    IcesiUser fromIcesiUserDTO(IcesiUserDTO userCreateDTO);

    @Mapping(target = "icesiRole", source = "icesiRole",ignore=true)
    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);
}
