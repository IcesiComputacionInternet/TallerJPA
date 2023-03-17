package com.edu.icesi.TallerJPA.mapper;

import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);

}
