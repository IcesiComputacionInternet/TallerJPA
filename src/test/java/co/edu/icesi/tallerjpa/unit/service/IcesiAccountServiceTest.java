package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.enums.TypeIcesiAccount;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiAccountMapperImpl;
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
    private IcesiAccountService icesiAccountService;
    private IcesiUserRepository icesiUserRepository;

    private IcesiAccountCreateDTO regularIcesiAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type(TypeIcesiAccount.REGULAR_ACCOUNT)
                .active(true)
                .icesiUserDTO(defaultIcesiUserCreateDTO())
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
                .accountId(null)
                .accountNumber("012-012345-01")
                .balance(500)
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
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name("Admin")
                .build();
    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name("Admin")
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
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper, icesiUserRepository);
    }

    @Test
    public void testCreateIcesiAccount(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.save(regularIcesiAccountCreateDTO());
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
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.save(regularIcesiAccountCreateDTO());
        verify(icesiAccountRepository, times(1)).save(argThat(x -> x.getAccountId() != null));
    }

    @Test
    public void testGenerateAccountNumber(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.save(regularIcesiAccountCreateDTO());
        Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{6}-[0-9]{2}");
        verify(icesiAccountRepository, times(1)).save(argThat(x ->
                        x.getAccountNumber() != null &&
                        pattern.matcher(x.getAccountNumber()).matches()));
    }

    @Test
    public void testCreateIcesiAccountWithBalanceMinorZero(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        icesiAccountCreateDTO.setBalance(-1);
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.save(icesiAccountCreateDTO));
        assertEquals("Accounts balance can't be below 0.", exception.getMessage());
    }

    @Test
    public void testCreateIcesiAccountDisableWithBalanceDifferentZero(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        icesiAccountCreateDTO.setBalance(1);
        icesiAccountCreateDTO.setActive(false);
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.save(icesiAccountCreateDTO));
        assertEquals("Account can only be disabled if the balance is 0.", exception.getMessage());
    }

    @Test
    public void testCreateAccountWithNotExistingEmail(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        when(icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserDTO().getEmail())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.save(icesiAccountCreateDTO));
        assertEquals("There is no user with the email "+icesiAccountCreateDTO.getIcesiUserDTO().getEmail(), exception.getMessage());
    }

    @Test
    public void testCreateAccountWithErrorsWithTheAccountNumber(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        when(icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserDTO().getEmail())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultIcesiAccount()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.save(icesiAccountCreateDTO));
        assertEquals("There were errors creating the account number, please try again later", exception.getMessage());
    }

    @Test
    public void testGenerateAccountNumberWithOneExistingAccountNumber(){
        IcesiAccountCreateDTO icesiAccountCreateDTO = regularIcesiAccountCreateDTO();
        when(icesiUserRepository.findByEmail(icesiAccountCreateDTO.getIcesiUserDTO().getEmail())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultIcesiAccount()), Optional.ofNullable(null));
        icesiAccountService.save(icesiAccountCreateDTO);
        Pattern pattern = Pattern.compile("[0-9]{3}-[0-9]{6}-[0-9]{2}");
        verify(icesiAccountRepository, times(2)).findByAccountNumber(any());
        verify(icesiAccountRepository, times(1)).save(argThat(x ->
                        x.getAccountNumber() != null &&
                        pattern.matcher(x.getAccountNumber()).matches()));
    }

    @Test
    public void testEnableAccount(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(icesiAccount));
        icesiAccountService.enableAccount(accountId);
        verify(icesiAccountRepository, times(1)).enableAccount(argThat(x -> x == accountId));
    }

    @Test
    public void testEnableNotExistingAccount(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiUserRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.enableAccount(accountId));
        assertEquals("There is no account with the id: "+accountId, exception.getMessage());
        verify(icesiAccountRepository, times(0)).enableAccount(any());
    }

    @Test
    public void testEnableAnEnabledAccount(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.enableAccount(accountId));
        assertEquals("The account was already enabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).enableAccount(any());
    }

    @Test
    public void testDisableExistingAccountWithBalanceZero(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(0);
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(icesiAccount));
        icesiAccountService.disableAccount(accountId);
        verify(icesiAccountRepository, times(1)).disableAccount(argThat(x -> x.equals(accountId)));
    }

    @Test
    public void testDisableExistingAccountWithBalanceDifferentOfZero(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.disableAccount(accountId));
        assertEquals("Account can only be disabled if the balance is 0.", exception.getMessage());
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void testDisableNotExistingAccount(){
        String accountId = "c34f11df-cda3-4d75-a74b-4d8c98d6074f";
        when(icesiUserRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.disableAccount(accountId));
        assertEquals("There is no account with the id: "+accountId, exception.getMessage());
        verify(icesiAccountRepository, times(0)).disableAccount(any());
    }

    @Test
    public void testWithdrawalMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        TransactionResultDTO transactionResultDTO = icesiAccountService.withdrawalMoney(transactionCreateDTO);
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
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("There is no account with the id: "+transactionCreateDTO.getSenderAccountId(), exception.getMessage());
    }

    @Test
    public void testWithdrawalMoneyWithNotEnoughMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        transactionCreateDTO.setAmount(icesiAccount.getBalance()+1);
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        Exception exception = assertThrows(RuntimeException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to withdraw. At most you can withdraw: " + icesiAccount.getBalance(), exception.getMessage());
    }

    @Test
    public void testWithdrawalMoneyWithDisabledAccount(){
        TransactionCreateDTO transactionCreateDTO = defaultWithdrawalTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        icesiAccount.setActive(false);
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        Exception exception = assertThrows(RuntimeException.class,() -> icesiAccountService.withdrawalMoney(transactionCreateDTO));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("The account "+icesiAccount.getAccountId()+" is disabled", exception.getMessage());
    }

    @Test
    public void testDepositMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        TransactionResultDTO transactionResultDTO = icesiAccountService.depositMoney(transactionCreateDTO);
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
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class,() -> icesiAccountService.depositMoney(transactionCreateDTO));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("There is no account with the id: "+transactionCreateDTO.getReceiverAccountId(), exception.getMessage());
    }

    @Test
    public void testDepositMoneyWithDisabledAccount(){
        TransactionCreateDTO transactionCreateDTO = defaultDepositTransactionCreateDTO();
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        icesiAccount.setActive(false);
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.ofNullable(icesiAccount));
        Exception exception = assertThrows(RuntimeException.class,() -> icesiAccountService.depositMoney(transactionCreateDTO));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("The account "+icesiAccount.getAccountId()+" is disabled", exception.getMessage());
    }


    @Test
    public void testTransferMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        TransactionResultDTO transactionResultDTO = icesiAccountService.transferMoney(transactionCreateDTO);
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
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        assertEquals("There is no account with the id: " + transactionCreateDTO.getSenderAccountId(), exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(1)).findById(any());
    }

    @Test
    public void testTransferMoneyWithNotExistingAccountReceiver(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        assertEquals("There is no account with the id: " + transactionCreateDTO.getReceiverAccountId(), exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlyReceiverUser(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        IcesiAccount icesiAccountReceiver = regularIcesiAccountCreateWith1000();
        icesiAccountReceiver.setType(TypeIcesiAccount.DEPOSIT_ONLY.toString());
        icesiAccountReceiver.setAccountId(UUID.fromString(transactionCreateDTO.getReceiverAccountId()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(icesiAccountReceiver));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        assertEquals("The account with id " + transactionCreateDTO.getReceiverAccountId() + " is marked as deposit only so no money can be transferred", exception.getMessage());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlySenderUser(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountSender = regularIcesiAccountCreateWith1000();
        icesiAccountSender.setType(TypeIcesiAccount.DEPOSIT_ONLY.toString());
        icesiAccountSender.setAccountId(UUID.fromString(transactionCreateDTO.getSenderAccountId()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        assertEquals("The account with id " + transactionCreateDTO.getSenderAccountId() + " is marked as deposit only so it can't transfers money", exception.getMessage());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferWithNotEnoughMoney(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setAmount(1001);
        IcesiAccount icesiAccount = regularIcesiAccountCreateWith1000();
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        assertEquals("Not enough money to transfer. At most you can transfer: " + icesiAccount.getBalance(), exception.getMessage());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferWithDisableAccountOfSender(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountSender = regularIcesiAccountCreateWith1000();
        icesiAccountSender.setActive(false);
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(icesiAccountSender));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        assertEquals("The account "+icesiAccountSender.getAccountId()+" is disabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testTransferWithDisableAccountOfReceiver(){
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        IcesiAccount icesiAccountReceiver = regularIcesiAccountCreateWith1000();
        icesiAccountReceiver.setActive(false);
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getSenderAccountId()))).thenReturn(Optional.of(regularIcesiAccountCreateWith1000()));
        when(icesiAccountRepository.findById(UUID.fromString(transactionCreateDTO.getReceiverAccountId()))).thenReturn(Optional.of(icesiAccountReceiver));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoney(transactionCreateDTO));
        assertEquals("The account "+icesiAccountReceiver.getAccountId()+" is disabled", exception.getMessage());
        verify(icesiAccountRepository, times(0)).updateBalance(anyLong(), any());
        verify(icesiAccountRepository, times(2)).findById(any());
    }

    @Test
    public void testGetAccountByAccountNumber(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        when(icesiAccountRepository.findByAccountNumber(argThat(x -> x.equals(icesiAccount.getAccountNumber())))).thenReturn(Optional.of(icesiAccount));
        IcesiAccountShowDTO icesiAccount1 = icesiAccountService.getAccountByAccountNumber(icesiAccount.getAccountNumber());
        assertEquals(icesiAccount.getAccountId(), icesiAccount1.getAccountId());
        verify(icesiAccountRepository, times(0)).findById(any());
        verify(icesiAccountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testGetAccountByAccountNumberNotFound(){
        String accountNumber = defaultIcesiAccount().getAccountNumber();
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiAccountService.getAccountByAccountNumber(accountNumber));
        assertEquals("There is no account with the number: " + accountNumber, exception.getMessage());
        verify(icesiAccountRepository, times(1)).findByAccountNumber(any());
    }
}
