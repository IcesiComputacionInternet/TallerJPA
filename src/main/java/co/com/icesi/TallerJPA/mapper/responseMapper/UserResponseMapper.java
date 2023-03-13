package co.com.icesi.TallerJPA.mapper.responseMapper;

import co.com.icesi.TallerJPA.dto.response.UserResponseDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
        UserResponseDTO fromICesiUSer(IcesiUser icesiUser);
}
