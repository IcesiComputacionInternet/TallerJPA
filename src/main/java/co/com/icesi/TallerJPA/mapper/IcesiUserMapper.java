package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromUserDto(IcesiUserDTO userDTO);
    IcesiUserDTO fromIcesiUser(IcesiUser user);
}
