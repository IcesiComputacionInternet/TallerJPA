package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.ResponseUserDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role",ignore=true)
    IcesiUser fromIcesiUserDTO(RequestUserDTO userDTO);
    @Mapping(target = "role", source = "role",ignore=true)
    ResponseUserDTO fromUserToRespUserDTO(IcesiUser icesiUser);
}
