package com.Icesi.TallerJPA.mapper;

import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);
     IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO);
}
