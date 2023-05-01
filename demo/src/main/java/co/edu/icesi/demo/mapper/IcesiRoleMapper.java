package co.edu.icesi.demo.mapper;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiRoleMapper {

    IcesiRole fromIcesiRoleDto(IcesiRoleDto icesiRoleDto);
    IcesiRoleDto fromIcesiRole(IcesiRole icesiRole);
}
