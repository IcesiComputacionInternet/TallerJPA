package co.com.icesi.tallerjpa.mapper;

import co.com.icesi.tallerjpa.dto.UserCreateDTO;
import co.com.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);
    UserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}
