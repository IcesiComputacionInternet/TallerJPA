package icesi.university.accountSystem.mapper;

import icesi.university.accountSystem.dto.IcesiRoleDTO;
import icesi.university.accountSystem.dto.RequestAccountDTO;
import icesi.university.accountSystem.dto.ResponseAccountDTO;
import icesi.university.accountSystem.dto.ResponseUserDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import icesi.university.accountSystem.dto.TransactionResultDTO;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-28T15:48:22-0500",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
@Component
public class IcesiAccountMapperImpl implements IcesiAccountMapper {

    @Override
    public IcesiAccount fromIcesiAccountDTO(RequestAccountDTO createdAccountDTO) {
        if ( createdAccountDTO == null ) {
            return null;
        }

        IcesiAccount.IcesiAccountBuilder icesiAccount = IcesiAccount.builder();

        if ( createdAccountDTO.getBalance() != null ) {
            icesiAccount.balance( createdAccountDTO.getBalance() );
        }
        icesiAccount.type( createdAccountDTO.getType() );

        return icesiAccount.build();
    }

    @Override
    public RequestAccountDTO fromIcesiAccount(IcesiAccount account) {
        if ( account == null ) {
            return null;
        }

        RequestAccountDTO.RequestAccountDTOBuilder requestAccountDTO = RequestAccountDTO.builder();

        requestAccountDTO.balance( account.getBalance() );
        requestAccountDTO.type( account.getType() );

        return requestAccountDTO.build();
    }

    @Override
    public ResponseAccountDTO fromAccountToSendAccountDTO(IcesiAccount account) {
        if ( account == null ) {
            return null;
        }

        ResponseAccountDTO.ResponseAccountDTOBuilder responseAccountDTO = ResponseAccountDTO.builder();

        responseAccountDTO.accountNumber( account.getAccountNumber() );
        responseAccountDTO.balance( account.getBalance() );
        responseAccountDTO.type( account.getType() );
        responseAccountDTO.active( account.isActive() );
        responseAccountDTO.user( icesiUserToResponseUserDTO( account.getUser() ) );

        return responseAccountDTO.build();
    }

    @Override
    public TransactionResultDTO fromTransactionOperation(TransactionOperationDTO operationDTO, String result) {
        if ( operationDTO == null && result == null ) {
            return null;
        }

        TransactionResultDTO transactionResultDTO = new TransactionResultDTO();

        if ( operationDTO != null ) {
            transactionResultDTO.setAccountFrom( operationDTO.getAccountFrom() );
            transactionResultDTO.setAccountTo( operationDTO.getAccountTo() );
            if ( operationDTO.getAmount() != null ) {
                transactionResultDTO.setAmount( String.valueOf( operationDTO.getAmount() ) );
            }
        }
        transactionResultDTO.setResult( result );

        return transactionResultDTO;
    }

    protected IcesiRoleDTO icesiRoleToIcesiRoleDTO(IcesiRole icesiRole) {
        if ( icesiRole == null ) {
            return null;
        }

        IcesiRoleDTO.IcesiRoleDTOBuilder icesiRoleDTO = IcesiRoleDTO.builder();

        icesiRoleDTO.roleId( icesiRole.getRoleId() );
        icesiRoleDTO.description( icesiRole.getDescription() );
        icesiRoleDTO.name( icesiRole.getName() );
        List<IcesiUser> list = icesiRole.getIcesiUsers();
        if ( list != null ) {
            icesiRoleDTO.icesiUsers( new ArrayList<IcesiUser>( list ) );
        }

        return icesiRoleDTO.build();
    }

    protected ResponseUserDTO icesiUserToResponseUserDTO(IcesiUser icesiUser) {
        if ( icesiUser == null ) {
            return null;
        }

        ResponseUserDTO.ResponseUserDTOBuilder responseUserDTO = ResponseUserDTO.builder();

        responseUserDTO.firstName( icesiUser.getFirstName() );
        responseUserDTO.lastName( icesiUser.getLastName() );
        responseUserDTO.email( icesiUser.getEmail() );
        responseUserDTO.password( icesiUser.getPassword() );
        responseUserDTO.phoneNumber( icesiUser.getPhoneNumber() );
        responseUserDTO.role( icesiRoleToIcesiRoleDTO( icesiUser.getRole() ) );

        return responseUserDTO.build();
    }
}
