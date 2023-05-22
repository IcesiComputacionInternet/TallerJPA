package co.com.icesi.TallerJpa.mapper;

import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    @Mapping(target = "icesiUser", source = "userEmail", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "active", ignore = true)
    IcesiAccount fromAccountDto(IcesiAccountRequestDTO icesiAccountRequestDTO);
    @Mapping(target = "user", source = "icesiUser")
    @Mapping(target = "user.role", source = "icesiUser.icesiRole")
    IcesiAccountResponseDTO fromIcesiAccountToResponse(IcesiAccount icesiAccount);
}
