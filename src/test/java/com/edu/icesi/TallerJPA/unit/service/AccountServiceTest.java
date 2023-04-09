package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.dto.TransactionDTO;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.mapper.AccountMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.repository.UserRepository;
import com.edu.icesi.TallerJPA.service.AccountService;
import com.edu.icesi.TallerJPA.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    @BeforeEach
    private void init() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
    }

    @Test
    public void testCreateAccount() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createIcesiUser()));

        accountService.save(createAccountDTO());

        verify(userRepository, times(1)).findByEmail(any());
        verify(userRepository, times(1)).findByPhoneNumber(any());
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(createIcesiAccount())));
    }

    @Test
    public void testCreateAccountWithUserNotFound(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());

        try {
            accountService.save(createAccountDTO());
            fail();
        }catch (RuntimeException exception){
            String messageOfException = exception.getMessage();
            assertEquals("User not found", messageOfException);
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testCreateAccountWithBalanceBelowZero() {

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createIcesiUser()));

        try {
            accountService.save(createAccountDTOWithBalanceBelowZero());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Balance can't be below 0", messageOfException);
        }
    }


    @Test
    public void testDisableExistingAccount() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createAccountWithBalanceZero()));

        AccountCreateDTO account = accountService.setToDisableState(createAccountWithBalanceZero().getAccountNumber());

        verify(accountRepository, times(1)).save(any());

        assertFalse(account.isActive());
    }

    @Test
    public void testDisableExistingAccountWithBalanceGreaterThanZero() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.setToDisableState(createIcesiAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("The account balance is not zero", exception.getMessage());
        }
    }

    @Test
    public void testDisableAccountWithSameStatus() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createAccountDisable()));

        try {
            accountService.setToDisableState(createAccountDisable().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("The account is already in that status", exception.getMessage());
        }
    }

    @Test
    public void testEnableExistingAccount() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createAccountDisable()));

        AccountCreateDTO account = accountService.setToEnableState(createAccountDisable().getAccountNumber());

        verify(accountRepository, times(1)).save(any());

        assertTrue(account.isActive());
    }

    @Test
    public void testEnableAccountWithSameStatus() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.setToEnableState(createIcesiAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("The account is already in that status", exception.getMessage());
        }
    }

    @Test
    public void testEnableNotExistingAccount() {

        AccountCreateDTO accountCreateDTO = createAccountDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());
        verify(accountRepository, times(0)).save(any());

        try {
            accountService.setToEnableState(accountCreateDTO.getAccountNumber());
            fail();

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account "+accountCreateDTO.getAccountNumber()+" not found", messageOfException);
        }

    }

    @Test
    public void testDisableNotExistingAccount() {

        AccountCreateDTO accountCreateDTO = createAccountWithBalanceZeroDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());
        verify(accountRepository, times(0)).save(any());

        try {
            accountService.setToDisableState(accountCreateDTO.getAccountNumber());
            fail();

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account "+accountCreateDTO.getAccountNumber()+" not found", messageOfException);
        }
    }

    @Test
    public void testWithdrawWithEnoughMoneyFromNormalAccount() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createNormalAccount()));

        TransactionDTO transaction = new TransactionDTO(createNormalAccount().getAccountNumber(), "", 3000L, "",
                0L, 0L);

        transaction = accountService.withdrawals(transaction);

        verify(accountRepository, times(1)).save(any());

        assertEquals("Successful withdrawal", transaction.getResult());
        assertEquals(2000L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testWithdrawWithEnoughMoneyFromDepositOnlyAccount() {

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(createIcesiAccount()));

        TransactionDTO transaction = new TransactionDTO(createIcesiAccount().getAccountNumber(), "", 3000L, "",
                0L, 0L);

        transaction = accountService.withdrawals(transaction);

        verify(accountRepository, times(1)).save(any());

        assertEquals("Successful withdrawal", transaction.getResult());
        assertEquals(2000L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testWithdrawWithInsufficientMoneyFromNormalAccount() {

        IcesiAccount account = createIcesiAccount();

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        TransactionDTO transaction = new TransactionDTO(account.getAccountNumber(), "", 8000L, "",
                0L, 0L);

        try {
            transaction = accountService.withdrawals(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("The account "+account.getAccountNumber()+" cannot perform the transaction. Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testWithdrawWithInsufficientMoneyFromDepositOnlyAccount() {

        IcesiAccount account = createIcesiAccount();

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        TransactionDTO transaction = new TransactionDTO(account.getAccountNumber(), "", 8000L, "",
                0L, 0L);

        try {
            transaction = accountService.withdrawals(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("The account "+account.getAccountNumber()+" cannot perform the transaction. Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testWithdrawWithAccountNotFound() {

        AccountCreateDTO accountCreateDTO = createAccountDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());

        TransactionDTO transaction = new TransactionDTO(accountCreateDTO.getAccountNumber(), "", 3000L, "",
                0L, 0L);
        try {
            accountService.withdrawals(transaction);
            fail();

        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account "+accountCreateDTO.getAccountNumber()+" not found", messageOfException);
        }

        verify(accountRepository, times(0)).save(any());

    }

    @Test
    public void testWithdrawWithInvalidValue() {

        IcesiAccount accountToDeposit = createNormalAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        TransactionDTO transaction = new TransactionDTO(accountToDeposit.getAccountNumber(), "", 0L, "",
                0L, 0L);

        try {
            transaction = accountService.withdrawals(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Invalid value for transaction. Value can't be less than zero or zero", exception.getMessage());
            assertEquals("", transaction.getResult());
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testDepositMoneyNormalAccount() {

        IcesiAccount accountToDeposit = createNormalAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        TransactionDTO transaction = new TransactionDTO(accountToDeposit.getAccountNumber(), "", 3000L, "",
                0L, 0L);
        transaction = accountService.depositMoney(transaction);

        verify(accountRepository, times(1)).save(any());
        assertEquals("Successful deposit", transaction.getResult());
        assertEquals(8000L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testDepositInvalidMoneyNormalAccount() {

        IcesiAccount accountToDeposit = createNormalAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        TransactionDTO transaction = new TransactionDTO(accountToDeposit.getAccountNumber(), "", -1000L, "",
                0L, 0L);

        try {
            transaction = accountService.depositMoney(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Invalid value for transaction. Value can't be less than zero or zero", exception.getMessage());
            assertEquals("", transaction.getResult());
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testDepositMoneyDepositOnlyAccount() {

        IcesiAccount accountToDeposit = createIcesiAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        TransactionDTO transaction = new TransactionDTO(accountToDeposit.getAccountNumber(), "", 3000L, "",
                0L, 0L);

        transaction = accountService.depositMoney(transaction);

        verify(accountRepository, times(1)).save(any());
        assertEquals("Successful deposit", transaction.getResult());
        assertEquals(8000L, transaction.getFinalBalanceSourceAccount());
    }

    @Test
    public void testDepositInvalidMoneyDepositOnlyAccount() {

        IcesiAccount accountToDeposit = createIcesiAccount();

        when(accountRepository.findByAccountNumber(accountToDeposit.getAccountNumber())).thenReturn(Optional.of(accountToDeposit));

        TransactionDTO transaction = new TransactionDTO(accountToDeposit.getAccountNumber(), "", -100L, "",
                0L, 0L);

        try {
            transaction = accountService.depositMoney(transaction);
            fail();
        }catch (RuntimeException exception){
            assertEquals("Invalid value for transaction. Value can't be less than zero or zero", exception.getMessage());
            assertEquals("", transaction.getResult());
        }

        verify(accountRepository, times(0)).save(any());
    }

    @Test
    public void testTransferMoneyToNormalAccount() {

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationAccount = createNormalAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        TransactionDTO transaction = new TransactionDTO(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 3000L, "",
                0L, 0L);

        transaction = accountService.transferMoney(transaction);

        verify(accountRepository, times(2)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());

        assertEquals("Successful transfer", transaction.getResult());
        assertEquals(2000L, transaction.getFinalBalanceSourceAccount());
        assertEquals(8000L, transaction.getFinalBalanceDestinationAccount());
    }

    @Test
    public void testTransferMoneyToNormalAccountFromInsufficientMoneyAccount() {

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationAccount = createNormalAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        TransactionDTO transaction = new TransactionDTO(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 6000L, "",
                0L, 0L);

        try{
            transaction = accountService.transferMoney(transaction);
            fail();

        }catch (RuntimeException exception){
            assertEquals("The account "+transaction.getSourceAccount()+" cannot perform the transaction. Insufficient money", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyToNormalAccountWithInvalidValue() {

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationAccount = createNormalAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        TransactionDTO transaction = new TransactionDTO(sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 0L, "",
                0L, 0L);

        try {
            transaction = accountService.transferMoney(transaction);
            fail();
        } catch (RuntimeException exception) {
            assertEquals("Invalid value for transaction. Value can't be less than zero or zero", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyToDepositOnlyAccount() {

        IcesiAccount sourceDepositOnlyAccount = createIcesiAccount();
        IcesiAccount destinationAccount = createIcesiAccount();

        when(accountRepository.findByAccountNumber(sourceDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(sourceDepositOnlyAccount));
        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));

        TransactionDTO transaction = new TransactionDTO(sourceDepositOnlyAccount.getAccountNumber(), destinationAccount.getAccountNumber(), 0L, "",
                0L, 0L);
        try {
            transaction = accountService.transferMoney(transaction);
            fail();
        } catch (RuntimeException exception) {
            assertEquals("It is not possible to make the transfer. At least one account is deposit only", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    @Test
    public void testTransferMoneyFromDepositOnlyAccount() {

        IcesiAccount sourceAccount = createNormalAccount();
        IcesiAccount destinationDepositOnlyAccount = createIcesiAccount();

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber(destinationDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(destinationDepositOnlyAccount));

        TransactionDTO transaction = new TransactionDTO(sourceAccount.getAccountNumber(), destinationDepositOnlyAccount.getAccountNumber(), 0L, "",
                0L, 0L);

        try {
            transaction = accountService.transferMoney(transaction);
            fail();
        } catch (RuntimeException exception) {
            assertEquals("It is not possible to make the transfer. At least one account is deposit only", exception.getMessage());
            assertEquals("", transaction.getResult());
        }
        verify(accountRepository, times(0)).save(any());
        verify(accountRepository, times(4)).findByAccountNumber(any());
    }

    public AccountCreateDTO createAccountDTO() {
        return AccountCreateDTO.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public AccountCreateDTO createAccountWithBalanceZeroDTO() {
        return AccountCreateDTO.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(0)
                .type("Deposit only")
                .active(true)
                .build();
    }

    public IcesiAccount createAccountWithBalanceZero() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(0)
                .type("Deposit only")
                .active(true)
                .build();
    }

    public IcesiAccount createNormalAccount() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Normal")
                .active(true)
                .build();
    }

    public IcesiAccount createAccountDisable() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Deposit Only")
                .active(false)
                .build();
    }

    public AccountCreateDTO createAccountDTOWithBalanceBelowZero() {
        return AccountCreateDTO.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(-1)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiAccount createIcesiAccount() {
        return IcesiAccount.builder()
                .accountNumber(accountService.sendToGenerateAccountNumbers())
                .balance(5000)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiUser createIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .build();
    }

}
