package co.com.icesi.TallerJPA.mapper;

import co.com.icesi.TallerJPA.dto.AccountResponseDTO;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountResponseMapper {

    AccountResponseDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
