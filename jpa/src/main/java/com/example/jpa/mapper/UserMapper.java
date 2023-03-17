package com.example.jpa.mapper;

import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.dto.UserResponseDTO;
import com.example.jpa.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role", ignore = true)
    IcesiUser fromUserRequestDTO(UserRequestDTO icesiUserDTO);

    @Mapping(target = "role", source = "role", ignore = true)
    UserRequestDTO fromUser(IcesiUser icesiUser);

    UserResponseDTO fromUserToUserResponseDTO(IcesiUser icesiUser);
}
