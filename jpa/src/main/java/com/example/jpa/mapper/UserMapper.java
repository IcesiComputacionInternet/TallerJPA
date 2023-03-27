package com.example.jpa.mapper;

import com.example.jpa.dto.UserDTO;
import com.example.jpa.model.IcesiUser;
import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //@Mapping(target = "role", source = "role", ignore = true)
    IcesiUser fromUserRequestDTO(UserDTO icesiUserDTO);

    //@Mapping(target = "role", source = "role", ignore = true)
    UserDTO fromUserToUserResponseDTO(IcesiUser icesiUser);
}
