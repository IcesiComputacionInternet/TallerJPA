package com.example.tallerjpa.unit.service.service;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.dto.TransactionResponseDTO;
import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.mapper.AccountMapper;
import com.example.tallerjpa.mapper.AccountMapperImpl;
import com.example.tallerjpa.model.AccountType;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.AccountRepository;
import com.example.tallerjpa.repository.UserRepository;
import com.example.tallerjpa.service.AccountService;
import com.example.tallerjpa.service.UserService;
import com.example.tallerjpa.unit.service.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;

    private IcesiAccount icesiAccount;

    private UserService userService;

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository, userRepository, accountMapper);
    }

    @Test
    public void testSaveAccount() {
        when(userRepository.searchByEmail(any())).thenReturn(Optional.of(defaultUser()));
        accountService.createAccount(defaultAccountDTO());
        verify(userRepository, times(1)).searchByEmail(any());
        verify(accountMapper, times(1)).fromAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(defaultAccount())));
    }

    @Test
    public void testSaveAccountWithUserNotFound() {
        when(userRepository.searchByEmail(any())).thenReturn(Optional.empty());

        try {
            accountService.createAccount(defaultAccountDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("User doesn't exists", message);
        }
    }

    @Test
    public void testSaveAccountWithNegativeBalance(){
        when(userRepository.searchByEmail(any())).thenReturn(Optional.of(defaultUser()));
        try{
            accountService.createAccount(defaultAccountDTO());
        }catch (RuntimeException e) {
            assertEquals("Balance can't be negative",e.getMessage());
        }
    }

    @Test
    public void testSetAccountNumberGenerated(){
        String number = accountService.generateAccountNumber();
        when(accountRepository.existsByAccountNumber(number)).thenReturn(false);
        IcesiAccount icesiAccount = defaultAccount();
        icesiAccount.setAccountNumber(number);
        assertNotNull(icesiAccount.getAccountNumber());
        assertEquals(number, icesiAccount.getAccountNumber());
        assertTrue(number.matches("^\\d{3}-\\d{6}-\\d{2}$"));
    }

    @Test
    public void testSetTypeAccountDeposit(){
        IcesiAccount icesiAccount = defaultAccount();
        accountService.setTypeAccount("deposit", icesiAccount);
        assertNotNull(icesiAccount.getType());
        assertEquals(AccountType.DEPOSIT, icesiAccount.getType());
    }

    @Test
    public void testSetTypeAccountDefault(){
        IcesiAccount icesiAccount = defaultAccount();
        accountService.setTypeAccount("default", icesiAccount);
        assertNotNull(icesiAccount.getType());
        assertEquals(AccountType.DEFAULT, icesiAccount.getType());
    }

    @Test
    public void testSetTypeAccountWhenTypeDoesNotExist(){
        IcesiAccount icesiAccount = defaultAccount();
        try {
            accountService.setTypeAccount("deposits", icesiAccount);
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Account type does not exist", message);
        }
    }

    @Test
    public void testDisableAccount(){
        IcesiAccount account = defaultAccount();
        account.setBalance(0);
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        assertEquals("The account was disabled", accountService.deactivateAccount(account.getAccountNumber()));
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void testEnableAccount(){
        IcesiAccount account = defaultAccount();
        account.setActive(false);
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        assertEquals("The account was enabled", accountService.activateAccount(account.getAccountNumber()));
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void testDisableAccountWithBalanceDifferentTo0(){
        when(accountRepository.getAccount(any())).thenReturn(Optional.ofNullable(defaultAccount()));
        try{
            accountService.deactivateAccount(defaultAccount().getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account can not be disabled",e.getMessage());
        }
    }

    @Test
    public void disableAccountWhenTheAccountDoesNotExist(){
        when(accountRepository.getAccount(any())).thenReturn(Optional.empty());
        try{
            accountService.deactivateAccount(defaultAccount().getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account was not found",e.getMessage());
        }
    }

    @Test
    public void enableAccountWhenTheAccountDoesNotExist(){
        when(accountRepository.getAccount(any())).thenReturn(Optional.empty());
        try{
            accountService.activateAccount(defaultAccount().getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account was not found",e.getMessage());
        }
    }

    @Test
    public void withdrawTest(){
        IcesiAccount account = defaultAccount();
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(account.getAccountNumber())
                .amount(100000L)
                .build();
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        accountService.withdrawMoney(transactionDTO);
        assertEquals(0L, account.getBalance());
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void withdrawTestWithLowBalance(){
        IcesiAccount account = defaultAccount();
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(account.getAccountNumber())
                .amount(150000L)
                .build();
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        try {
            accountService.withdrawMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The account doesn't have enough money", e.getMessage());
        }
    }

    @Test
    public void withdrawTestWithInactiveAccount(){
        IcesiAccount account = defaultAccount();
        account.setActive(false);
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(account.getAccountNumber())
                .amount(100000L)
                .build();
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        try {
            accountService.withdrawMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("This account is not active, so can't be done transactions", e.getMessage());
        }
    }
    @Test
    public void depositTest(){
        IcesiAccount account = defaultAccount();
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(account.getAccountNumber())
                .amount(50000L)
                .build();
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        accountService.depositMoney(transactionDTO);
        assertEquals(150000L, account.getBalance());
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void depositTestWithInactiveAccount(){
        IcesiAccount account = defaultAccount();
        account.setActive(false);
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(account.getAccountNumber())
                .amount(100000L)
                .build();
        when(accountRepository.getAccount(any())).thenReturn(Optional.of(account));
        try {
            accountService.depositMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("This account is not active, so can't be done transactions", e.getMessage());
        }
    }

    @Test
    public void transferTest(){
        IcesiAccount originAccount = defaultAccount();
        originAccount.setType(AccountType.valueOf("DEFAULT"));
        IcesiAccount destinationAccount = defaultAccount();
        destinationAccount.setType(AccountType.valueOf("DEFAULT"));
        destinationAccount.setAccountNumber("200-123809-00");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(originAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(50000L)
                .build();
        when(accountRepository.getAccount(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.getAccount(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));
        TransactionResponseDTO responseDTO = accountService.transferMoney(transactionDTO);
        assertEquals("The transfer was done successfully", responseDTO.getResult());
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(originAccount)));
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(destinationAccount)));
    }

    @Test
    public void transferTestWithNonExistentAccounts(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber("100-234567-99")
                .destinationAccountNumber("200-123809-00")
                .amount(50000L)
                .build();
        try{
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The account was not found", e.getMessage());
        }
    }

    @Test
    public void transferTestWithTypeOfOriginAccountInDeposit(){
        IcesiAccount originAccount = defaultAccount();
        IcesiAccount destinationAccount = defaultAccount();
        destinationAccount.setType(AccountType.valueOf("DEFAULT"));
        destinationAccount.setAccountNumber("200-123809-00");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(originAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(50000L)
                .build();
        when(accountRepository.getAccount(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.getAccount(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));
        try{
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The origin account is only for deposits, so can't transfer", e.getMessage());
        }
    }

    @Test
    public void transferTestWithTypeOfDestinationAccountInDeposit(){
        IcesiAccount originAccount = defaultAccount();
        originAccount.setType(AccountType.valueOf("DEFAULT"));
        IcesiAccount destinationAccount = defaultAccount();
        destinationAccount.setAccountNumber("200-123809-00");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(originAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(50000L)
                .build();
        when(accountRepository.getAccount(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.getAccount(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));
        try{
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The destination account is only for deposits, so can't transfer", e.getMessage());
        }
    }

    @Test
    public void transferTestWithOriginAccountInactive(){
        IcesiAccount originAccount = defaultAccount();
        originAccount.setActive(false);
        originAccount.setType(AccountType.valueOf("DEFAULT"));
        IcesiAccount destinationAccount = defaultAccount();
        destinationAccount.setType(AccountType.valueOf("DEFAULT"));
        destinationAccount.setAccountNumber("200-123809-00");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(originAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(50000L)
                .build();
        when(accountRepository.getAccount(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.getAccount(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));
        try{
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The origin account is not active", e.getMessage());
        }
    }

    @Test
    public void transferTestWithDestinationAccountInactive(){
        IcesiAccount originAccount = defaultAccount();
        originAccount.setType(AccountType.valueOf("DEFAULT"));
        IcesiAccount destinationAccount = defaultAccount();
        destinationAccount.setType(AccountType.valueOf("DEFAULT"));
        destinationAccount.setActive(false);
        destinationAccount.setAccountNumber("200-123809-00");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(originAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(50000L)
                .build();
        when(accountRepository.getAccount(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.getAccount(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));
        try{
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The destination account is not active", e.getMessage());
        }
    }

    @Test
    public void transferTestWithLowBalance(){
        IcesiAccount originAccount = defaultAccount();
        originAccount.setType(AccountType.valueOf("DEFAULT"));
        IcesiAccount destinationAccount = defaultAccount();
        destinationAccount.setType(AccountType.valueOf("DEFAULT"));
        destinationAccount.setAccountNumber("200-123809-00");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .originAccountNumber(originAccount.getAccountNumber())
                .destinationAccountNumber(destinationAccount.getAccountNumber())
                .amount(500000L)
                .build();
        when(accountRepository.getAccount(originAccount.getAccountNumber())).thenReturn(Optional.of(originAccount));
        when(accountRepository.getAccount(destinationAccount.getAccountNumber())).thenReturn(Optional.of(destinationAccount));
        try{
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (RuntimeException e){
            assertEquals("The account doesn't have enough money for do this transfer", e.getMessage());
        }
    }

    private AccountDTO defaultAccountDTO(){
        return AccountDTO.builder()
                .balance(100000L)
                .type(AccountType.valueOf("DEPOSIT"))
                .active(true)
                .emailUser("juanosorio@hotmail.com")
                .build();
    }

    private IcesiAccount defaultAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("100-234567-99")
                .balance(100000L)
                .type(AccountType.valueOf("DEPOSIT"))
                .active(true)
                .icesiUser(defaultUser())
                .build();

    }
    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("d6391e20-8c56-4378-9e72-013c6f2f6f88"))
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("12345678")
                .password("password")
                .icesiRole(defaultRole())
                .accountList(new ArrayList<>())
                .build();
    }

    private UserDTO defaultUserDTO(){
        return UserDTO.builder()
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("12345678")
                .password("password")
                .role("Student")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Student")
                .description("Student of the Icesi University")
                .build();
    }


}
