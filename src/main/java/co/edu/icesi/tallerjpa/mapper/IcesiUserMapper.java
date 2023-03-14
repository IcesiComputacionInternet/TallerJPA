package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.CreateIcesiUserDTO;
import co.edu.icesi.tallerjpa.dto.ShowIcesiUserDTO;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser fromCreateIcesiUserDTO(CreateIcesiUserDTO createIcesiUserDTO);
    CreateIcesiUserDTO fromIcesiUserToCreate(IcesiUser icesiUser);

    IcesiRole fromShowIcesiUserDTO(ShowIcesiUserDTO showIcesiUserDTO);

    ShowIcesiUserDTO fromIcesiUserToShow(IcesiUser icesiUser);
}
