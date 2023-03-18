package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromIcesiUserCreateDTO(IcesiUserCreateDTO icesiUserCreateDTO);
    IcesiUserCreateDTO fromIcesiUser(IcesiUser icesiUser);
}
