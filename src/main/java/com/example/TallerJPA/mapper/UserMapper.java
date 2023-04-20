package com.example.TallerJPA.mapper;
import com.example.TallerJPA.dto.UserDTO;
import com.example.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    IcesiUser fromIcesiUserDTO(UserDTO userDTO);
    UserDTO fromIcesiUser(IcesiUser icesiUser);
}
