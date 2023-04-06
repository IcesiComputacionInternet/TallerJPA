package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);

    @Mapping(target = "roleName", expression = "java(icesiUser.getRole().getName())")
    UserCreateDTO fromIcesiUser(IcesiUser icesiUser);

}
