package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.UserDTO;
import co.edu.icesi.demo.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUserDTO(UserDTO userDTO);

    @Mapping(target = "roleName", expression = "java(icesiUser.getRole().getName())")
    UserDTO fromIcesiUser(IcesiUser icesiUser);

}
