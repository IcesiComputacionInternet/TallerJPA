package co.com.icesi.demojpa.mapper;

import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.UserResponseDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    IcesiUser fromIcesiUser (UserCreateDTO icesiUserDTO);

    UserCreateDTO fromIcesiUserDTO (IcesiUser icesiUser);

    UserResponseDTO toResponse (IcesiUser icesiUser);
}