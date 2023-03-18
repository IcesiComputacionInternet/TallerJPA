package com.example.tallerjpa.mapper;

import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromUserDTO (UserDTO userDTO);
    UserDTO fromIcesiUser (IcesiUser icesiUser);

}
