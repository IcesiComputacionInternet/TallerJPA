package com.Icesi.TallerJPA.mapper;

import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    @Mapping(target = "icesiRole", source = "icesiRole",ignore=true)
    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);
    @Mapping(target = "icesiRole", source = "icesiRole",ignore=true)
     IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO);
}
