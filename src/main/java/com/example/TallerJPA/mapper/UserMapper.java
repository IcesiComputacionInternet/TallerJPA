package com.example.TallerJPA.mapper;
import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);
    UserCreateDTO fromIcesUser(IcesiUser icesiUser);
}
