package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.CreatedUserDTO;
import co.com.icesi.tallerjpa.dto.SendUserDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role",ignore=true)
    IcesiUser fromUserDTO(CreatedUserDTO icesiUserDTO);
    @Mapping(target = "role", source = "role",ignore=true)
    CreatedUserDTO fromUser(IcesiUser icesiUser);
    SendUserDTO fromUserToSendUserDTO(IcesiUser icesiUser);
}
