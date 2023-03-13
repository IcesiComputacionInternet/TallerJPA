package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role",ignore = true)
    IcesiUser fromIcesiUserDTO(UserCreateDTO userCreateDTO);

    @Mapping(target = "role", source = "role",ignore = true)
    UserCreateDTO fromICesiUSer(IcesiUser icesiUser);
}
