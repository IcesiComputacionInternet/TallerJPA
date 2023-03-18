package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromUserDto(IcesiUserCreateDTO icesiUserCreateDTO);

    IcesiUserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}
