package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiAccountDTO;
import icesi.university.accountSystem.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromIcesiAccountDTO(IcesiAccountDTO icesiAccountDTO);

    IcesiAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
