package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {

    IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO);

    IcesiUserDTO toIcesiUserDTO(IcesiUser icesiUser);

}
