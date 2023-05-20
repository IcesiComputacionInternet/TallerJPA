package co.com.icesi.icesiAccountSystem.mapper;

import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.ResponseUserDTO;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface UserMapper {
    @Mapping(target = "role", source = "roleName",ignore=true)
    IcesiUser fromUserDTO(RequestUserDTO requestUserDTO);
    @Mapping(target = "role", source = "role",ignore=true)
    ResponseUserDTO fromUserToResponseUserDTO(IcesiUser icesiUser);
}
