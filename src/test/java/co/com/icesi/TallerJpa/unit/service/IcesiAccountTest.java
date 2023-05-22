package co.com.icesi.TallerJpa.unit.service;

import co.com.icesi.TallerJpa.dto.IcesiAccountRequestDTO;
import co.com.icesi.TallerJpa.dto.IcesiAccountResponseDTO;
import co.com.icesi.TallerJpa.dto.TransactionDTO;
import co.com.icesi.TallerJpa.enums.AccountType;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.error.exception.IcesiException;
import co.com.icesi.TallerJpa.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJpa.mapper.IcesiAccountMapperImpl;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiAccountRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import co.com.icesi.TallerJpa.service.IcesiAccountService;
import co.com.icesi.TallerJpa.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class IcesiAccountTest {
    private IcesiAccountService icesiAccountService;
    private IcesiAccountRepository icesiAccountRepository;
    private IcesiUserRepository icesiUserRepository;
    private IcesiAccountMapper icesiAccountMapper;
    @BeforeEach
    public void init(){
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapperImpl.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper, icesiUserRepository);
    }

    @Test
    @DisplayName("Create account Happy Path.")
    public void testCreateIcesiAccountHappyPath(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.saveAccount(defaultIcesiAccountDTO());
        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiAccountMapper,times(1)).fromAccountDto(any());
        verify(icesiAccountMapper,times(1)).fromIcesiAccountToResponse(any());
        verify(icesiAccountRepository,times(1)).save(argThat(new IcesiAccountMatcher(defaultIcesiAccount())));
    }

    @Test
    @DisplayName("Create account with user not found.")
    public void testCreateIcesiAccountWithUserNotFound(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.saveAccount(defaultIcesiAccountDTO()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND, icesiError.getStatus());
        assertEquals("User: "+ defaultIcesiUser().getEmail() +" not found"
                , icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiUser with Email: "+ defaultIcesiUser().getEmail() +" not found"
                , icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiAccountMapper,times(0)).fromAccountDto(any());
        verify(icesiAccountMapper,times(0)).fromIcesiAccountToResponse(any());
        verify(icesiAccountRepository,times(0)).save(argThat(new IcesiAccountMatcher(defaultIcesiAccount())));
    }

    @Test
    @DisplayName("Create Account with balance lower than zero")
    public void testCreateIcesiAccountWithBalanceLowerThanZero(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));

        IcesiAccountRequestDTO icesiAccountRequestDTO = defaultIcesiAccountDTO();
        icesiAccountRequestDTO.setBalance(-1L);
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(-1L);
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.saveAccount(icesiAccountRequestDTO));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals("The balance can't be lower than 0"
                , icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("field Balance lower than 0", icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiAccountMapper,times(0)).fromAccountDto(any());
        verify(icesiAccountMapper,times(0)).fromIcesiAccountToResponse(any());
        verify(icesiAccountRepository,times(0)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    @DisplayName("Get account by account number Happy Path.")
    public void testGetIcesiAccountHappyPath(){
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccountResponseDTO icesiAccount =icesiAccountService.getAccountByAccountNumber("12345");
        assertEquals(defaultIcesiAccount().getAccountNumber(), icesiAccount.getAccountNumber());
        verify(icesiAccountRepository,times(1)).findByAccountNumber(any());
    }

    @Test
    @DisplayName("Get account by account number with account not found.")
    public void testGetIcesiAccountNotFound(){
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.getAccountByAccountNumber("12345"));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND, icesiError.getStatus());
        assertEquals("Account not found"
                , icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiAccount with accountNumber: "+ defaultIcesiAccount().getAccountNumber() +" not found"
                , icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber(any());
    }

    @Test
    @DisplayName("Enable icesi account Happy Path")
    public void testIcesiAccountEnableHappyPath(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(true);
        icesiAccountService.enableAccount("12345",userId);
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(1)).enableByAccountNumber("12345");
    }

    @Test
    @DisplayName("Enable icesi account with user not the owner")
    public void testIcesiAccountEnableNotOwner(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(false);
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.enableAccount("12345",userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.UNAUTHORIZED, icesiError.getStatus());
        assertEquals("Can't modified account", icesiException.getMessage());
        assertEquals("ERR_401", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("Unauthorized", icesiError.getDetails().get(0).getErrorMessage());


        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(0)).enableByAccountNumber("12345");
    }

    @Test
    @DisplayName("Disable icesi account Happy Path")
    public void testIcesiAccountDisableHappyPath(){
        UUID userId = defaultIcesiAccount().getAccountId();
        String accountNumber = defaultIcesiAccount().getAccountNumber();
        when(icesiAccountRepository.isIcesiAccountOwner(userId,accountNumber)).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber(accountNumber)).thenReturn(false);
        TransactionDTO transactionDTO = icesiAccountService.disableAccount(accountNumber,userId);
        assertEquals("Account disabled", transactionDTO.getMessage());
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,accountNumber);
        verify(icesiAccountRepository,times(1)).disableByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Disable icesi account with balance higher than 0.")
    public void testIcesiAccountDisableBalanceHigherThanZero(){
        UUID userId = defaultIcesiAccount().getAccountId();
        String accountNumber = defaultIcesiAccount().getAccountNumber();
        when(icesiAccountRepository.isIcesiAccountOwner(userId,accountNumber)).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber(accountNumber)).thenReturn(true);
        TransactionDTO transactionDTO = icesiAccountService.disableAccount(accountNumber,userId);
        assertEquals("Account can't be disable, balance higher than 0", transactionDTO.getMessage());
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,accountNumber);
        verify(icesiAccountRepository,times(1)).disableByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Disable icesi account with user not the owner")
    public void testIcesiAccountDisableNotOwner(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(false);
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.disableAccount("12345",userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.UNAUTHORIZED, icesiError.getStatus());
        assertEquals("Can't modified account", icesiException.getMessage());
        assertEquals("ERR_401", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("Unauthorized", icesiError.getDetails().get(0).getErrorMessage());


        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(0)).disableByAccountNumber("12345");
    }

    @Test
    @DisplayName("Withdrawal of money from icesi account Happy Path")
    public void testIcesiAccountWithdrawalHappyPath(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(defaultIcesiAccount()));
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber("12345")).thenReturn(true);
        icesiAccountService.withdrawal(defaultTransactionDTO(), userId);
        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(1)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).updateBalance(18L,"12345");
    }

    @Test
    @DisplayName("Withdrawal of money from icesi account not found")
    public void testIcesiAccountWithdrawalNotFound(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.withdrawal(defaultTransactionDTO(), userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND, icesiError.getStatus());
        assertEquals("Account not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiAccount with accountNumber: "+defaultIcesiAccount().getAccountNumber()+" not found",
                icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(0)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).updateBalance(18L,"12345");
    }

    @Test
    @DisplayName("Withdrawal of money from icesi account user not owner")
    public void testIcesiAccountWithdrawalNotOwner(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(defaultIcesiAccount()));
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(false);
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.withdrawal(defaultTransactionDTO(), userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.UNAUTHORIZED, icesiError.getStatus());
        assertEquals("Can't modified account", icesiException.getMessage());
        assertEquals("ERR_401", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("Unauthorized", icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(0)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).updateBalance(18L,"12345");
    }

    @Test
    @DisplayName("Withdrawal of money from icesi account disable")
    public void testIcesiAccountWithdrawalDisabled(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(defaultIcesiAccount()));
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber("12345")).thenReturn(false);
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.withdrawal(defaultTransactionDTO(), userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals("The account "+defaultIcesiAccount().getAccountNumber()+" is disabled",
                icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("field Active in account: "+defaultIcesiAccount().getAccountNumber()+
                ", is disabled", icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(1)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).updateBalance(18L,"12345");
    }

    @Test
    @DisplayName("Withdrawal of money from icesi account not enough money")
    public void testIcesiAccountWithdrawalNotEnoughMoney(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(defaultIcesiAccount()));
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber("12345")).thenReturn(true);
        TransactionDTO transactionDTO = defaultTransactionDTO();
        transactionDTO.setAmount(21L);
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.withdrawal(transactionDTO, userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals("Theres not enough money", icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("field Balance has not enough money", icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(1)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).updateBalance(-1L,"12345");
    }

    @Test
    @DisplayName("Deposit of money in icesi account Happy Path")
    public void testIcesiAccountDepositHappyPath(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(defaultIcesiAccount()));
        when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber("12345")).thenReturn(true);
        icesiAccountService.deposit(defaultTransactionDTO(), userId);
        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(1)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(1)).updateBalance(22L,"12345");
    }

     @Test
     @DisplayName("Deposit of money in icesi account not found")
     public void testIcesiAccountDepositNotFound(){
        UUID userId = defaultIcesiAccount().getAccountId();
        when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());
        IcesiException icesiException = assertThrows(IcesiException.class,
                 () -> icesiAccountService.deposit(defaultTransactionDTO(), userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND, icesiError.getStatus());
        assertEquals("Account not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiAccount with accountNumber: "+defaultIcesiAccount().getAccountNumber()+" not found",
        icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).isIcesiAccountOwner(userId,"12345");
        verify(icesiAccountRepository,times(0)).isActiveByAccountNumber("12345");
        verify(icesiAccountRepository,times(0)).updateBalance(22L,"12345");
     }

     @Test
     @DisplayName("Deposit of money in icesi account user not owner")
     public void testIcesiAccountDepositNotOwner(){
         UUID userId = defaultIcesiAccount().getAccountId();
         when(icesiAccountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(defaultIcesiAccount()));
         when(icesiAccountRepository.isIcesiAccountOwner(userId,"12345")).thenReturn(false);
         IcesiException icesiException = assertThrows(IcesiException.class,
                 () -> icesiAccountService.deposit(defaultTransactionDTO(), userId));
         IcesiError icesiError = icesiException.getError();

         assertEquals(1,icesiError.getDetails().size());
         assertEquals(HttpStatus.UNAUTHORIZED, icesiError.getStatus());
         assertEquals("Can't modified account", icesiException.getMessage());
         assertEquals("ERR_401", icesiError.getDetails().get(0).getErrorCode());
         assertEquals("Unauthorized", icesiError.getDetails().get(0).getErrorMessage());

         verify(icesiAccountRepository,times(1)).findByAccountNumber("12345");
         verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,"12345");
         verify(icesiAccountRepository,times(0)).isActiveByAccountNumber("12345");
         verify(icesiAccountRepository,times(0)).updateBalance(22L,"12345");
     }

     @Test
     @DisplayName("Deposit of money in icesi account disabled")
     public void testIcesiAccountDepositDisabled(){
         UUID userId = defaultIcesiAccount().getAccountId();
         String accountNumber = defaultIcesiAccount().getAccountNumber();
         when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(defaultIcesiAccount()));
         when(icesiAccountRepository.isIcesiAccountOwner(userId,accountNumber)).thenReturn(true);
         when(icesiAccountRepository.isActiveByAccountNumber(accountNumber)).thenReturn(false);
         IcesiException icesiException = assertThrows(IcesiException.class,
                 () -> icesiAccountService.withdrawal(defaultTransactionDTO(), userId));
         IcesiError icesiError = icesiException.getError();

         assertEquals(1,icesiError.getDetails().size());
         assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
         assertEquals("The account "+accountNumber+" is disabled", icesiException.getMessage());
         assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
         assertEquals("field Active in account: "+accountNumber+", is disabled", icesiError.getDetails().get(0).getErrorMessage());

         verify(icesiAccountRepository,times(1)).findByAccountNumber(accountNumber);
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,accountNumber);
        verify(icesiAccountRepository,times(1)).isActiveByAccountNumber(accountNumber);
         verify(icesiAccountRepository,times(0)).updateBalance(18L,accountNumber);
     }

     @Test
     @DisplayName("Transfer of money between icesi accounts Happy Path")
     public void testIcesiAccountTransferHappyPath(){
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        when(icesiAccountRepository.isIcesiAccountOwner(any(),any())).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber(any())).thenReturn(true);
        TransactionDTO transactionDTO = defaultTransferDTO();
        transactionDTO = icesiAccountService.transfer(transactionDTO, any());
        assertEquals("The transfer was successful",transactionDTO.getMessage());
        verify(icesiAccountRepository,times(2)).findByAccountNumber(any());
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(any(),any());
        verify(icesiAccountRepository,times(2)).isActiveByAccountNumber(any());
        verify(icesiAccountRepository,times(2)).updateBalance(any(),any());
     }

     @Test
     @DisplayName("Transfer of money between icesi accounts origin deposit only")
     public void testIcesiAccountTranferOriginDepositOnly(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setType(AccountType.DEPOSIT_ONLY);
        UUID userId = UUID.fromString("52b5dc21-f21b-48de-8fdd-621bc26e88e7");
        String accountNumber = "12345";
        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));
        when(icesiAccountRepository.isIcesiAccountOwner(userId, accountNumber)).thenReturn(true);
        when(icesiAccountRepository.isActiveByAccountNumber(accountNumber)).thenReturn(true);
        TransactionDTO transferDTO = defaultTransferDTO();
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiAccountService.transfer(transferDTO, userId));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals("Account is deposit only", icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("field AccountType is deposit only", icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiAccountRepository,times(1)).findByAccountNumber(accountNumber);
        verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,accountNumber);
        verify(icesiAccountRepository,times(1)).isActiveByAccountNumber(accountNumber);
        verify(icesiAccountRepository,times(0)).updateBalance(15L,accountNumber);
     }

     @Test
     @DisplayName("Transfer of money between icesi accounts destiny deposit only")
     public void testIcesiAccountTransferDestinyDepositOnly(){
         IcesiAccount icesiAccount = defaultIcesiAccountDestiny();
         icesiAccount.setType(AccountType.DEPOSIT_ONLY);
         UUID userId = UUID.fromString("52b5dc21-f21b-48de-8fdd-621bc26e88e7");
         String accountNumberOrigin = "12345";
         String accountNumberDestiny = "54321";
         when(icesiAccountRepository.findByAccountNumber(accountNumberOrigin)).thenReturn(Optional.of(defaultIcesiAccount()));
         when(icesiAccountRepository.isIcesiAccountOwner(userId, accountNumberOrigin)).thenReturn(true);
         when(icesiAccountRepository.isActiveByAccountNumber(accountNumberOrigin)).thenReturn(true);
         when(icesiAccountRepository.findByAccountNumber(accountNumberDestiny)).thenReturn(Optional.of(icesiAccount));
         when(icesiAccountRepository.isActiveByAccountNumber(accountNumberDestiny)).thenReturn(true);
         TransactionDTO transferDTO = defaultTransferDTO();
         IcesiException icesiException = assertThrows(IcesiException.class,
                 () -> icesiAccountService.transfer(transferDTO, userId));
         IcesiError icesiError = icesiException.getError();

         assertEquals(1,icesiError.getDetails().size());
         assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
         assertEquals("Account is deposit only", icesiException.getMessage());
         assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
         assertEquals("field AccountType is deposit only", icesiError.getDetails().get(0).getErrorMessage());

         verify(icesiAccountRepository,times(1)).findByAccountNumber(accountNumberOrigin);
         verify(icesiAccountRepository,times(1)).isIcesiAccountOwner(userId,accountNumberOrigin);
         verify(icesiAccountRepository,times(1)).isActiveByAccountNumber(accountNumberOrigin);
         verify(icesiAccountRepository,times(1)).findByAccountNumber(accountNumberDestiny);
         verify(icesiAccountRepository,times(1)).isActiveByAccountNumber(accountNumberDestiny);
         verify(icesiAccountRepository,times(0)).updateBalance(15L,accountNumberOrigin);
         verify(icesiAccountRepository,times(0)).updateBalance(25L,accountNumberDestiny);
     }


    private IcesiAccountRequestDTO defaultIcesiAccountDTO(){
        return IcesiAccountRequestDTO.builder()
                .balance(20L)
                .type(AccountType.NORMAL)
                .userEmail("jaime.enrique@hotmail.com").build();
    }
    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.fromString("2c3ec5ac-acb0-4913-862e-0dc5d6b7cb5d"))
                .accountNumber("12345")
                .balance(20L)
                .type(AccountType.NORMAL)
                .active(true)
                .icesiUser(defaultIcesiUser()).build();
    }
    private IcesiAccount defaultIcesiAccountDestiny(){
        return IcesiAccount.builder()
                .accountId(UUID.fromString("2c3ec5ac-acb0-4913-862e-0dc5d6b7cb5c"))
                .accountNumber("54321")
                .balance(20L)
                .type(AccountType.NORMAL)
                .active(true)
                .icesiUser(defaultIcesiUserDestiny()).build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Jaime")
                .lastName("Enrique")
                .email("jaime.enrique@hotmail.com")
                .phoneNumber("3209867514")
                .password("password")
                .icesiRole(defaultIcesiRole()).build();
    }
    private IcesiUser defaultIcesiUserDestiny(){
        return IcesiUser.builder()
                .firstName("Jose")
                .lastName("Antonio")
                .email("jose.antonio@hotmail.com")
                .phoneNumber("3209867515")
                .password("password")
                .icesiRole(defaultIcesiRole()).build();
    }
    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("2c3ec5ac-acb0-4913-862e-0dc5d6b7cb5d"))
                .name("Profesor_Colaborador")
                .description("Profesor de la universidad").build();
    }
    private TransactionDTO defaultTransactionDTO(){
        return TransactionDTO.builder()
                .accountNumberOrigin("12345")
                .amount(2L)
                .build();
    }
    private TransactionDTO defaultTransferDTO(){
        return TransactionDTO.builder()
                .accountNumberOrigin("12345")
                .accountNumberDestiny("54321")
                .amount(5L)
                .build();
    }
}
