package co.com.icesi.demojpa.mapper;

import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.UserResponseDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    //@Mapping(target = "role", source = "role",ignore=true)
    IcesiUser fromUserCreateDTO(UserCreateDTO icesiUserDTO);
    //@Mapping(target = "role", source = "role",ignore=true)
    UserCreateDTO fromUser(IcesiUser icesiUser);
    UserResponseDTO fromUserToSendUserDTO(IcesiUser icesiUser);
}