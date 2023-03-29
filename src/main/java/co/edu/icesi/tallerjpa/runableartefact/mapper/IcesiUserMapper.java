package co.edu.icesi.tallerjpa.runableartefact.mapper;

import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {

    IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO);

    @Mapping(target = "password", source = "icesiUser.password", ignore = true)
    IcesiUserDTO toIcesiUserDTO(IcesiUser icesiUser);

}
