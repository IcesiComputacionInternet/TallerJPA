package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.dto.IcesiAccountDTOResponse;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IcesiAccountMapper {
    IcesiAccount fromAccountDTO(IcesiAccountDTO accountDTO);
    IcesiAccountDTOResponse fromIcesiAccount(IcesiAccount account);
}
