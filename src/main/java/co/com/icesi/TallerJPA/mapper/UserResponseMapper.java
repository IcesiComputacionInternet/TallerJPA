package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.UserResponseDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
        UserResponseDTO fromICesiUSer(IcesiUser icesiUser);
}
