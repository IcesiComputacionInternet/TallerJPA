package com.icesi.TallerJPA.mapper;

import com.icesi.TallerJPA.dto.IcesiUserDTO;
import com.icesi.TallerJPA.dto.IcesiUserResponseDTO;
import com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUser (IcesiUserDTO icesiUserDTO);

    IcesiUserDTO fromIcesiUserDTO (IcesiUser icesiUser);

    IcesiUserResponseDTO toResponse (IcesiUser icesiUser);


}
