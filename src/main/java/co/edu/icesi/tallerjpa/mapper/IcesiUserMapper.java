package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromCreateIcesiUserDTO(IcesiUserCreateDTO icesiUserCreateDTO);
    IcesiUserCreateDTO fromIcesiUserToCreate(IcesiUser icesiUser);

    IcesiRole fromShowIcesiUserDTO(IcesiUserShowDTO icesiUserShowDTO);

    IcesiUserShowDTO fromIcesiUserToShow(IcesiUser icesiUser);
}
