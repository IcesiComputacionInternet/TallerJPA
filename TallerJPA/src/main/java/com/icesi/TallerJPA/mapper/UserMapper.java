package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.IcesiUserDTO;
import com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUser (IcesiUserDTO icesiUserDTO);

    IcesiUserDTO fromIcesiUserDTO (IcesiUser icesiUser);
}
