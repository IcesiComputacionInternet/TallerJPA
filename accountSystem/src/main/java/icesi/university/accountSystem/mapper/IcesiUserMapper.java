package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiUserDTO;
import icesi.university.accountSystem.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromIcesiUserDTO(IcesiUserDTO icesiUserDTO);

    IcesiUserDTO fromIcesiUser(IcesiUser icesiUser);
}
