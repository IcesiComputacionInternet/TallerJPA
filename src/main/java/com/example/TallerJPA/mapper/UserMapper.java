package com.example.TallerJPA.mapper;
import com.example.TallerJPA.dto.UserCreateDTO;
import com.example.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface UserMapper {
    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);
    UserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}
