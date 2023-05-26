package co.com.icesi.jpataller.mapper.response;

import co.com.icesi.jpataller.dto.response.IcesiResponseUserDTO;
import co.com.icesi.jpataller.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserResponseMapper {
    IcesiResponseUserDTO fromIcesUser(IcesiUser icesiUser);
}