package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiUserResponseDTO;
import co.com.icesi.TallerJpa.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    @Mapping(target = "icesiRole", source = "role", ignore = true)
    IcesiUser fromUserDto(IcesiUserRequestDTO icesiUserRequestDTO);
    @Mapping(target = "role", source = "icesiRole")
    IcesiUserResponseDTO fromIcesiUserToResponse(IcesiUser icesiUser);
}
