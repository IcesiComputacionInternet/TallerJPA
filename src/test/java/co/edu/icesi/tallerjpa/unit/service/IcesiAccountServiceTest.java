package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiException;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapperImpl;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {

    private IcesiAccountRepository icesiAccountRepository;
    private IcesiAccountMapper icesiAccountMapper;
    private IcesiUserMapper icesiUserMapper;
    private IcesiAccountService icesiAccountService;
    private IcesiUserRepository icesiUserRepository;

    private IcesiAccountCreateDTO regularIcesiAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT)
                .active(true)
                .icesiUserEmail(defaultIcesiUserCreateDTO().getEmail())
                .build();
    }

    private IcesiAccount regularIcesiAccountCreateWith1000(){
        return IcesiAccount.builder()
                .balance(1000)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.fromString("2d032e11-2bec-4983-9c35-3a00d8750809"))
                .accountNumber("012-012345-01")
                .balance(500)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    private IcesiAccount defaultIcesiAccountWith0(){
        return IcesiAccount.builder()
                .accountId(null)
                .accountNumber("012-012345-01")
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }

    private IcesiUserCreateDTO defaultIcesiUserCreateDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRoleCreateDTO(defaultIcesiRoleCreateDTO())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c75bf838-8f38-403d-b64f-cca8b7a181d8"))
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .build();
    }

    private IcesiUser adminIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("7e39d68f-dc03-4634-92d1-37bb4b1865e3"))
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(adminIcesiRole())
                .icesiAccounts(null)
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name(NameIcesiRole.USER.toString())
                .build();
    }

    private IcesiRole adminIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name(NameIcesiRole.ADMIN.toString())
                .build();

    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name(NameIcesiRole.USER.toString())
                .build();
    }

    private TransactionCreateDTO defaultTransferTransactionCreateDTO(){
        return TransactionCreateDTO.builder()
                .senderAccountId("c34f11df-cda3-4d75-a74b-4d8c98d6074f")
                .receiverAccountId("c34f11df-1234-4d75-a74b-4d8c98d6074f")
                .amount(500)
                .build();
    }

    private TransactionCreateDTO defaultWithdrawalTransactionCreateDTO(){
        return TransactionCreateDTO.builder()
                .senderAccountId("c34f11df-cda3-4d75-a74b-4d8c98d6074f")
                .amount(500)
                .build();
    }

    private TransactionCreateDTO defaultDepositTransactionCreateDTO(){
        return TransactionCreateDTO.builder()
                .receiverAccountId("c34f11df-1234-4d75-a74b-4d8c98d6074f")
                .amount(500)
                .build();
    }


    @BeforeEach
    private void init(){
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapperImpl.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapper.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper, icesiUserMapper, icesiUserRepository);
    }

    @Test
    public void testCreateIcesiAccount(){
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        when(icesiAccountRepository.save(any())).thenReturn(defaultIcesiAccountWith0());
        icesiAccountService.save(regularIcesiAccountCreateDTO(), icesiUser.getUserId().toString());
        IcesiAccount icesiAccount = IcesiAccount.builder()
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT.toString())
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
        verify(icesiAccountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    public void testGenerateUUID(){
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        when(icesiAccountRepository.save(any())).thenReturn(defaultIcesiAccountWith0());
        icesiAccountService.save(regularIcesiAccountCreateDTO(), icesiUser.getUserId().toString());

        verify(icesiAccountRepository, times(1)).save(argThat(x -> x.getAccountId() != null));
    }

    @Test
    public void testGenerateAccountNumber(){
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        when(icesiAccountRepository.save(any())).thenReturn(defaultIcesiAccountWith0());
        icesiAccountService.save(regularIcesiAccountCreateDTO(), icesiUser.getUserId().toString());
        Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{6}-[0-9]{2}");
        verify(icesiAccountRepository, times(1)).save(argThat(x ->
                        x.getAccountNumber() != null &&
                        pattern.matcher(x.getAccountNumber()).matches()));
    }

    @Test
    public void testCreateIcesiAccountWithBalanceMinorZero(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        icesiAccountCreateDTO.setBalance(-1);
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.save(icesiAccountCreateDTO, defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field balance: Accounts balance can't be below 0", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("Accounts balance can't be below 0", exception.getMessage());

    }

    @Test
    public void testCreateIcesiAccountDisableWithBalanceDifferentZero(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        icesiAccountCreateDTO.setBalance(1);
        icesiAccountCreateDTO.setActive(false);
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.save(icesiAccountCreateDTO, defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: Account can only be disabled if the balance is 0", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("Account can only be disabled if the balance is 0", exception.getMessage());
    }

    @Test
    public void testCreateAccountWithNotExistingEmail(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        when(icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserEmail())).thenReturn(Optional.ofNullable(null));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.save(icesiAccountCreateDTO, defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("Icesi user with email: "+icesiAccountCreateDTO.getIcesiUserEmail()+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no user with the email "+icesiAccountCreateDTO.getIcesiUserEmail(), exception.getMessage());
    }

    @Test
    public void testCreateAccountWithErrorsWithTheAccountNumber(){
        IcesiUser icesiUser = adminIcesiUser();
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        when(icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserEmail())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultIcesiAccount()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.save(icesiAccountCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals("There were errors creating the account number, please try again later", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("Internal server error", exception.getMessage());
        assertEquals(500, icesiError.getStatus().value());
    }

    @Test
    public void testGenerateAccountNumberWithOneExistingAccountNumber(){
        IcesiUser icesiUser = adminIcesiUser();
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        when(icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserEmail())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultIcesiAccount()), Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        when(icesiAccountRepository.save(any())).thenReturn(defaultIcesiAccountWith0());
        icesiAccountService.save(icesiAccountCreateDTO, icesiUser.getUserId().toString());
        Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{6}-[0-9]{2}");
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
        verify(icesiAccountRepository, times(1)).save(argThat(x ->
                        x.getAccountNumber() != null &&
                        pattern.matcher(x.getAccountNumber()).matches()));
    }

    @Test
    public void testEnableAccount(){
        IcesiUser icesiUser = adminIcesiUser();
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        when(icesiAccountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        icesiAccountService.enableAccount(icesiAccount.getAccountNumber(), icesiUser.getUserId().toString());
        verify(icesiAccountRepository, times(1)).enableAccount(argThat(x -> x.equals(icesiAccount.getAccountId().toString())));
    }

    @Test
    public void testEnableNotExistingAccount(){
        IcesiUser icesiUser = adminIcesiUser();
        String accountNumber = defaultIcesiAccount().getAccountNumber();
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.enableAccount(accountNumber, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with the number: "+accountNumber+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with the number: "+accountNumber, exception.getMessage());
        verify(icesiAccountRepository, times(0)).enableAccount(any());
    }

    @Test
    public void testEnableAnEnabledAccount(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        String accountNumber = icesiAccount.getAccountNumber();
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.enableAccount(accountNumber, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: The account was already enabled", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account was already enabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).enableAccount(any());
    }

    @Test
    public void testDisableExistingAccountWithBalanceZero(){
        IcesiUser icesiUser = adminIcesiUser();
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(0);
        when(icesiAccountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        icesiAccountService.disableAccount(icesiAccount.getAccountNumber(), icesiUser.getUserId().toString());
        verify(icesiAccountRepository, times(1)).disableAccount(argThat(x -> x.equals(icesiAccount.getAccountId().toString())));
    }

    @Test
    public void testDisableExistingAccountWithBalanceDifferentOfZero(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        IcesiUser icesiUser = adminIcesiUser();
        String accountNumber = icesiAccount.getAccountNumber();
        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.disableAccount(accountNumber, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive and balance: Account can only be disabled if the balance is 0", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("Account can only be disabled if the balance is 0", exception.getMessage());
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void testDisableNotExistingAccount(){
        IcesiUser icesiUser = adminIcesiUser();
        IcesiAccount icesiAccount = defaultIcesiAccount();
        String accountNumber = icesiAccount.getAccountNumber();
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        when(icesiAccountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.ofNullable(null));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.disableAccount(accountNumber, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with the number: "+accountNumber + " not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with the number: "+accountNumber, exception.getMessage());
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void testWithdrawalMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        TransactionResultDTO transactionResultDTO = icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUser.getUserId().toString());
        long newBalance = icesiAccount.getBalance() - transactionCreateDTO.getAmount();

        verify(icesiAccountRepository, times(1))
                .updateBalance(longThat(x -> x == newBalance), argThat(x -> x.equals(transactionCreateDTO.getSenderAccountId())));

        assertEquals("The withdrawal was successful", transactionResultDTO.getResult());
        assertEquals(newBalance, transactionResultDTO.getBalance());
        assertEquals(transactionCreateDTO.getSenderAccountId(), transactionResultDTO.getSenderAccountId());
    }

    @Test
    public void testWithdrawalMoneyWithNotExistingAccount(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));


        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with id: "+transactionCreateDTO.getSenderAccountId() + " not found", icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("There is no account with the id: "+transactionCreateDTO.getSenderAccountId(), exception.getMessage());
    }

    @Test
    public void testWithdrawalMoneyWithNotEnoughMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        transactionCreateDTO.setAmount(icesiAccount.getBalance()+1);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field balance: Not enough money to withdraw. At most you can withdraw: "+icesiAccount.getBalance(), icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance(), exception.getMessage());
    }

    @Test
    public void testWithdrawalMoneyWithDisabledAccount(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        icesiAccount.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: " + "The account "+icesiAccount.getAccountId()+" is disabled" , icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("The account "+icesiAccount.getAccountId()+" is disabled", exception.getMessage());
    }

    @Test
    public void testDepositMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        TransactionResultDTO transactionResultDTO = icesiAccountService.depositMoney(transactionCreateDTO, icesiUser.getUserId().toString());
        long newBalance = icesiAccount.getBalance() + transactionCreateDTO.getAmount();

        verify(icesiAccountRepository, times(1))
                .updateBalance(longThat(x -> x == newBalance), argThat(x -> x.equals(transactionCreateDTO.getReceiverAccountId())));

        assertEquals("The deposit was successful", transactionResultDTO.getResult());
        assertEquals(newBalance, transactionResultDTO.getBalance());
        assertEquals(transactionCreateDTO.getReceiverAccountId(), transactionResultDTO.getReceiverAccountId());
    }

    @Test
    public void testDepositMoneyWithNotExistingAccount(){
        TransactionCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.depositMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with id: "+transactionCreateDTO.getReceiverAccountId() + " not found", icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("There is no account with the id: "+transactionCreateDTO.getReceiverAccountId(), exception.getMessage());
    }

    @Test
    public void testDepositMoneyWithDisabledAccount(){
        TransactionCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        icesiAccount.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.depositMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: "+"The account "+icesiAccount.getAccountId()+" is disabled", icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("The account "+icesiAccount.getAccountId()+" is disabled", exception.getMessage());
    }


    @Test
    public void testTransferMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        TransactionResultDTO transactionResultDTO = icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString());
        assertEquals(500, transactionResultDTO.getBalance());
        assertEquals(transactionCreateDTO.getSenderAccountId(), transactionResultDTO.getSenderAccountId());
        assertEquals(transactionCreateDTO.getReceiverAccountId(), transactionResultDTO.getReceiverAccountId());
        assertEquals("The transfer was successful", transactionResultDTO.getResult());
        verify(icesiAccountRepository, times(1)).updateBalance(longThat((x -> x == 500)), argThat(x -> x.equals(transactionCreateDTO.getSenderAccountId())));
        verify(icesiAccountRepository, times(1)).updateBalance(longThat((x -> x == 1500)), argThat(x -> x.equals(transactionCreateDTO.getReceiverAccountId())));
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferMoneyWithNotExistingAccountSender(){
        IcesiUser icesiUser = adminIcesiUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with id: "+transactionCreateDTO.getSenderAccountId()+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with the id: " + transactionCreateDTO.getSenderAccountId(), exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(1)).findById(any());
    }

    @Test
    public void testTransferMoneyWithNotExistingAccountReceiver(){
        IcesiUser icesiUser = adminIcesiUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with id: "+transactionCreateDTO.getReceiverAccountId()+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with the id: " + transactionCreateDTO.getReceiverAccountId(), exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlyReceiverUser(){
        IcesiUser icesiUser = adminIcesiUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiAccount icesiAccountReceiver = regularIcesiAccountCreateWith1000();
        icesiAccountReceiver.setType(TypeIcesiAccount.DEPOSIT_ONLY.toString());
        icesiAccountReceiver.setAccountId(UUID.fromString(transactionCreateDTO.getReceiverAccountId()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(icesiAccountReceiver));
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field type: "+"The account with id " + transactionCreateDTO.getReceiverAccountId() + " is marked as deposit only so no money can be transferred", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account with id " + transactionCreateDTO.getReceiverAccountId() + " is marked as deposit only so no money can be transferred", exception.getMessage());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlySenderUser(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountSender = regularIcesiAccountCreateWith1000();
        icesiAccountSender.setType(TypeIcesiAccount.DEPOSIT_ONLY.toString());
        icesiAccountSender.setAccountId(UUID.fromString(transactionCreateDTO.getSenderAccountId()));
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field type: "+"The account with id " + transactionCreateDTO.getSenderAccountId() + " is marked as deposit only so it can't transfers money", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account with id " + transactionCreateDTO.getSenderAccountId() + " is marked as deposit only so it can't transfers money", exception.getMessage());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferWithNotEnoughMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setAmount(1001);
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field balance: "+"Not enough money to transfer. At most you can transfer: " + icesiAccount.getBalance(), icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to transfer. At most you can transfer: " + icesiAccount.getBalance(), exception.getMessage());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferWithDisableAccountOfSender(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountSender = regularIcesiAccountCreateWith1000();
        icesiAccountSender.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: "+"The account "+icesiAccountSender.getAccountId()+" is disabled", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account "+icesiAccountSender.getAccountId()+" is disabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferWithDisableAccountOfReceiver(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountReceiver = regularIcesiAccountCreateWith1000();
        icesiAccountReceiver.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(icesiAccountReceiver));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: "+"The account "+icesiAccountReceiver.getAccountId()+" is disabled", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account "+icesiAccountReceiver.getAccountId()+" is disabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testGetAccountByAccountNumber(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(argThat(x -> x.equals(icesiAccount.getAccountNumber())))).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiAccountShowDTO icesiAccount1 = icesiAccountService.getAccountByAccountNumber(icesiAccount.getAccountNumber(), icesiUser.getUserId().toString());
        assertEquals(icesiAccount.getAccountId(), icesiAccount1.getAccountId());
        verify(icesiAccountRepository, times(0)).findById(any());
        verify(icesiAccountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testGetAccountByAccountNumberNotFound(){
        String accountNumber = defaultIcesiAccount().getAccountNumber();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.getAccountByAccountNumber(accountNumber, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with the number: "+accountNumber + " not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with the number: " + accountNumber, exception.getMessage());
        verify(icesiAccountRepository, times(1)).findByAccountNumber(any());
    }
}
