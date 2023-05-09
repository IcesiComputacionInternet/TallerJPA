package co.com.icesi.demojpa.mapper.response;


import co.com.icesi.demojpa.dto.response.ResponseAccountDTO;
import co.com.icesi.demojpa.model.IcesiAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountResponseMapper {

    ResponseAccountDTO fromIcesiAccount(IcesiAccount icesiAccount);
}
