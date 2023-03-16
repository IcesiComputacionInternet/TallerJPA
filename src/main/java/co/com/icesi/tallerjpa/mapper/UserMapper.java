package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role",ignore=true)
    IcesiUser fromUserDTO(RequestUserDTO icesiUserDTO);
    @Mapping(target = "role", source = "role",ignore=true)
    RequestUserDTO fromUser(IcesiUser icesiUser);
    ResponseUserDTO fromUserToSendUserDTO(IcesiUser icesiUser);
}
