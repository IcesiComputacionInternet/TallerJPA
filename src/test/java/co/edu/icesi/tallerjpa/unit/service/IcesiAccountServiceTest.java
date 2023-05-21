package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiException;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapperImpl;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiAccount;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.regex.Pattern;

import static co.edu.icesi.tallerjpa.util.DTOBuilder.*;
import static co.edu.icesi.tallerjpa.util.ModelBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {

    private IcesiAccountRepository icesiAccountRepository;
    private IcesiAccountMapper icesiAccountMapper;
    private IcesiUserMapper icesiUserMapper;
    private IcesiAccountService icesiAccountService;
    private IcesiUserRepository icesiUserRepository;
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
        assertEquals("account with number: "+accountNumber+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with number: "+accountNumber, exception.getMessage());
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
        assertEquals("account with number: "+accountNumber + " not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with number: "+accountNumber, exception.getMessage());
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void testWithdrawalMoney(){
        TransactionWithOneAccountCreateDTO transactionWithOneAccountCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        TransactionWithOneAccountCreateDTO transactionResultDTO = icesiAccountService.withdrawalMoney(transactionWithOneAccountCreateDTO, icesiUser.getUserId().toString());
        long newBalance = icesiAccount.getBalance() - transactionWithOneAccountCreateDTO.getAmount();

        verify(icesiAccountRepository, times(1))
                .updateBalance(longThat(x -> x == newBalance), argThat(x -> x.equals(icesiAccount.getAccountId().toString())));

        assertEquals(newBalance, transactionResultDTO.getAmount());
        assertEquals(transactionWithOneAccountCreateDTO.getAccountNumber(), transactionResultDTO.getAccountNumber());
    }

    @Test
    public void testWithdrawalMoneyWithNotExistingAccount(){
        TransactionWithOneAccountCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));


        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with number: "+transactionCreateDTO.getAccountNumber() + " not found", icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("There is no account with number: "+transactionCreateDTO.getAccountNumber(), exception.getMessage());
    }

    @Test
    public void testWithdrawalMoneyWithNotEnoughMoney(){
        TransactionWithOneAccountCreateDTO transactionWithOneAccountCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        transactionWithOneAccountCreateDTO.setAmount(icesiAccount.getBalance()+1);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.withdrawalMoney(transactionWithOneAccountCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field balance: Not enough money to withdraw. At most you can withdraw: "+icesiAccount.getBalance(), icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance(), exception.getMessage());
    }

    @Test
    public void testWithdrawalMoneyWithDisabledAccount(){
        TransactionWithOneAccountCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        icesiAccount.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: " + "The account "+icesiAccount.getAccountNumber()+" is disabled" , icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("The account "+icesiAccount.getAccountNumber()+" is disabled", exception.getMessage());
    }

    @Test
    public void testDepositMoney(){
        TransactionWithOneAccountCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getAccountNumber())).thenReturn(Optional.ofNullable(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        TransactionWithOneAccountCreateDTO transactionResultDTO = icesiAccountService.depositMoney(transactionCreateDTO, icesiUser.getUserId().toString());
        long newBalance = icesiAccount.getBalance() + transactionCreateDTO.getAmount();

        verify(icesiAccountRepository, times(1))
                .updateBalance(longThat(x -> x == newBalance), argThat(x -> x.equals(icesiAccount.getAccountId().toString())));

        assertEquals(newBalance, transactionResultDTO.getAmount());
        assertEquals(transactionCreateDTO.getAccountNumber(), transactionResultDTO.getAccountNumber());
    }

    @Test
    public void testDepositMoneyWithNotExistingAccount(){
        TransactionWithOneAccountCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getAccountNumber())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.depositMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with number: "+transactionCreateDTO.getAccountNumber() + " not found", icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("There is no account with number: "+transactionCreateDTO.getAccountNumber(), exception.getMessage());
    }

    @Test
    public void testDepositMoneyWithDisabledAccount(){
        TransactionWithOneAccountCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        icesiAccount.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(icesiAccount.getAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class,() -> icesiAccountService.depositMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: "+"The account "+icesiAccount.getAccountNumber()+" is disabled", icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("The account "+icesiAccount.getAccountNumber()+" is disabled", exception.getMessage());
    }


    @Test
    public void testTransferMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiUser icesiUser = adminIcesiUser();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.of(icesiAccount));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        TransactionResultDTO transactionResultDTO = icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString());
        assertEquals(500, transactionResultDTO.getAmount());
        assertEquals(transactionCreateDTO.getSenderAccountNumber(), transactionResultDTO.getSenderAccountNumber());
        assertEquals(transactionCreateDTO.getReceiverAccountNumber(), transactionResultDTO.getReceiverAccountNumber());
        assertEquals("The transfer was successful", transactionResultDTO.getResult());
        verify(icesiAccountRepository, times(1)).updateBalance(longThat((x -> x == 500)), argThat(x -> x.equals(icesiAccount.getAccountId().toString())));
        verify(icesiAccountRepository, times(1)).updateBalance(longThat((x -> x == 1500)), argThat(x -> x.equals(icesiAccount.getAccountId().toString())));
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyWithNotExistingAccountSender(){
        IcesiUser icesiUser = adminIcesiUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with number: "+transactionCreateDTO.getSenderAccountNumber()+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with number: " + transactionCreateDTO.getSenderAccountNumber(), exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyWithNotExistingAccountReceiver(){
        IcesiUser icesiUser = adminIcesiUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("account with number: "+transactionCreateDTO.getReceiverAccountNumber()+" not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with number: " + transactionCreateDTO.getReceiverAccountNumber(), exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlyReceiverUser(){
        IcesiUser icesiUser = adminIcesiUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountReceiver = regularIcesiAccountCreateWith1000();
        icesiAccountReceiver.setType(TypeIcesiAccount.DEPOSIT_ONLY.toString());
        icesiAccountReceiver.setAccountNumber(transactionCreateDTO.getReceiverAccountNumber());

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.of(icesiAccountReceiver));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field type: "+"The account with number " + transactionCreateDTO.getReceiverAccountNumber() + " is marked as deposit only so no money can be transferred", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account with number " + transactionCreateDTO.getReceiverAccountNumber() + " is marked as deposit only so no money can be transferred", exception.getMessage());
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlySenderUser(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountSender = regularIcesiAccountCreateWith1000();
        icesiAccountSender.setType(TypeIcesiAccount.DEPOSIT_ONLY.toString());
        icesiAccountSender.setAccountNumber(transactionCreateDTO.getSenderAccountNumber());
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field type: "+"The account with number " + transactionCreateDTO.getSenderAccountNumber() + " is marked as deposit only so it can't transfers money", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account with number " + transactionCreateDTO.getSenderAccountNumber() + " is marked as deposit only so it can't transfers money", exception.getMessage());
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void testTransferWithNotEnoughMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setAmount(1001);
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field balance: "+"Not enough money to transfer. At most you can transfer: " + icesiAccount.getBalance(), icesiError.getDetails().get(0).getErrorMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to transfer. At most you can transfer: " + icesiAccount.getBalance(), exception.getMessage());
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void testTransferWithDisableAccountOfSender(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountSender = regularIcesiAccountCreateWith1000();
        icesiAccountSender.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: "+"The account "+icesiAccountSender.getAccountNumber()+" is disabled", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account "+icesiAccountSender.getAccountNumber()+" is disabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void testTransferWithDisableAccountOfReceiver(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountReceiver = regularIcesiAccountCreateWith1000();
        icesiAccountReceiver.setActive(false);
        IcesiUser icesiUser = adminIcesiUser();

        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getSenderAccountNumber())).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findByAccountNumber(transactionCreateDTO.getReceiverAccountNumber())).thenReturn(Optional.of(icesiAccountReceiver));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO, icesiUser.getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field isActive: "+"The account "+icesiAccountReceiver.getAccountNumber()+" is disabled", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("The account "+icesiAccountReceiver.getAccountNumber()+" is disabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
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
        assertEquals("account with number: "+accountNumber + " not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no account with number: " + accountNumber, exception.getMessage());
        verify(icesiAccountRepository, times(1)).findByAccountNumber(any());
    }
}
