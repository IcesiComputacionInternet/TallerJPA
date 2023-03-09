package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.UserDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromUserDTO(UserDTO icesiUserDTO);
    UserDTO fromUser(IcesiUser icesiUser);
}
