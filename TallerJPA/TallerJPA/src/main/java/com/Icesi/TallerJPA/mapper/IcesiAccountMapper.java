package com.Icesi.TallerJPA.mapper;

import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.IcesiRoleDTO;
import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO);
}
