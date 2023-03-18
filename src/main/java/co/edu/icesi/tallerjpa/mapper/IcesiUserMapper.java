package co.edu.icesi.tallerjpa.mapper;

import co.edu.icesi.tallerjpa.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiUserMapper {
    IcesiUser toIcesiUser(IcesiUserDTO icesiUserDTO);
    IcesiUserDTO fromModel(IcesiUser icesiUser);

}
