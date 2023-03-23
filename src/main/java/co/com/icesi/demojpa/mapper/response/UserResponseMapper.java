package co.com.icesi.demojpa.mapper.response;

import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    ResponseUserDTO fromIcesUser(IcesiUser icesiUser);
}
