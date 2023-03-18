package co.com.icesi.icesiAccountSystem.mapper;

import co.com.icesi.icesiAccountSystem.dto.UserDTO;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserMapper {
    IcesiUser fromUserDTO(UserDTO userDTO);
}
