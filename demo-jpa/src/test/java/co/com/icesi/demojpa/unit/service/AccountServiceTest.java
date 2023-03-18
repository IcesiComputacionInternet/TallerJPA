package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.dto.RoleCreateDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = spy(AccountMapper.class);
        accountService = new AccountService(userRepository, accountRepository, accountMapper);
    }

    @Test
    public void testSaveAccount(){
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(defaultCreateUser()));
        accountService.save(createDefaultDTOPositiveBalanceAccount());
        IcesiAccount expectedAccount= createDefaultPositiveBalanceAccount();

        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(expectedAccount)));
    }

    @Test
    public void testSaveAccountUserDoesNotExist(){
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        try{
            accountService.save(createDefaultDTOPositiveBalanceAccount());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User does not exist",message);
        }
    }

    @Test
    public void testSaveAccountNegativeBalance(){
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(defaultCreateUser()));

        try{
            accountService.save(createDefaultDTONegativeBalanceAccount());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Balance must be greater than 0",message);
        }

    }

    @Test
    public void testDisableAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefault0BalanceAccount()));
        accountService.disableAccount(createDefaultDTO0BalanceAccount().getAccountNumber());
        IcesiAccount expectedAccount= accountRepository.findByNumber(createDefaultDTO0BalanceAccount().getAccountNumber()).get();
        assertEquals(expectedAccount.isActive(), false);
    }

    @Test
    public void testDisableAccountNot0Balance(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            accountService.disableAccount(createDefaultDTOPositiveBalanceAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account must have 0 balance to be disabled",message);
        }
    }

    @Test
    public void enableNotExistentAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.enableAccount(createDefaultDTOPositiveBalanceAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account does not exist",message);
        }
    }

    @Test
    public void withdraw(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        accountService.withdraw(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), 1000);
        IcesiAccount expectedAccount= accountRepository.findByNumber(createDefaultDTOPositiveBalanceAccount().getAccountNumber()).get();
        assertEquals(expectedAccount.getBalance(), 999000);
    }

    @Test
    public void withdrawNotExistentAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.withdraw(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), 1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account does not exist",message);
        }
    }

    @Test
    public void withdrawNotActiveAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultDisabledAccount()));
        try{
            accountService.withdraw(createDefaultDTODisabledAccount().getAccountNumber(), 1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account is disabled",message);
        }
    }

    @Test
    public void withdrawMoreThanBalance(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            accountService.withdraw(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), 1000001);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account must have balance greater than the amount to withdraw",message);
        }
    }

    @Test
    public void deposit(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        accountService.deposit(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), 1000);
        IcesiAccount expectedAccount= accountRepository.findByNumber(createDefaultDTOPositiveBalanceAccount().getAccountNumber()).get();
        assertEquals(expectedAccount.getBalance(), 1001000);
    }

    @Test
    public void depositNotExistentAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.deposit(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), 1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account does not exist",message);
        }
    }

    @Test
    public void depositNotActiveAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultDisabledAccount()));
        try{
            accountService.deposit(createDefaultDTODisabledAccount().getAccountNumber(), 1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account is disabled",message);
        }
    }

    @Test
    public void depositNegativeAmount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            accountService.deposit(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), -1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Amount must be greater than 0",message);
        }
    }

    @Test
    public void transfer() {

        when(accountRepository.findByNumber(createDefaultPositiveBalanceAccount().getAccountNumber())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        when(accountRepository.findByNumber(createDefault0BalanceAccount().getAccountNumber())).thenReturn(Optional.ofNullable(createDefault0BalanceAccount()));
        when(accountRepository.save(any())).thenReturn(createDepositAccount());

        IcesiAccount sourceAccount = accountRepository.findByNumber(createDefaultPositiveBalanceAccount().getAccountNumber()).get();
        IcesiAccount targetAccount = accountRepository.findByNumber(createDefault0BalanceAccount().getAccountNumber()).get();
        accountService.transferMoney(sourceAccount.getAccountNumber(),targetAccount.getAccountNumber() ,1000);

        verify(accountRepository, times(2)).save(any());

        assertEquals(999000,sourceAccount.getBalance());

        assertEquals(1000,targetAccount.getBalance());

    }

    @Test
    public void transferNotExistentSourceAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.transferMoney(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), createDefaultDTO0BalanceAccount().getAccountNumber(), 1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("One or both accounts do not exist",message);
        }
    }

    @Test void transferDepositAccount(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDepositAccount()));
        try{
            accountService.transferMoney(createDepositAccount().getAccountNumber(), createDefaultDTO0BalanceAccount().getAccountNumber(), 1000);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("One or both accounts are deposit accounts, " +
                    "you can't transfer money to this type of accounts",message);
        }
    }

    @Test
    public void testTransferNotEnoughBalance(){
        when(accountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            accountService.transferMoney(createDefaultDTOPositiveBalanceAccount().getAccountNumber(), createDefaultDTO0BalanceAccount().getAccountNumber(), 1000001);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Source account must have balance greater than the amount to transfer",message);
        }
    }


    private IcesiAccount createDefaultPositiveBalanceAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-10")
                .type("Default")
                .user(defaultCreateUser())
                .balance(1000000)
                .active(true)
                .build();
    }

    private AccountCreateDTO createDefaultDTOPositiveBalanceAccount(){
        return AccountCreateDTO.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-10")
                .type("Default")
                .user(defaultCreateUserDTO())
                .balance(1000000)
                .active(true)
                .build();
    }

    private IcesiAccount createDepositAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-11")
                .type("Deposit")
                .user(defaultCreateUser())
                .balance(1000000)
                .active(true)
                .build();
    }

    private AccountCreateDTO createDepositDTOAccount(){
        return AccountCreateDTO.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-11")
                .type("Deposit")
                .user(defaultCreateUserDTO())
                .balance(1000000)
                .active(true)
                .build();
    }

    private IcesiAccount createDefault0BalanceAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-12")
                .type("Default")
                .user(defaultCreateUser())
                .balance(0)
                .active(true)
                .build();
    }

    private AccountCreateDTO createDefaultDTO0BalanceAccount(){
        return AccountCreateDTO.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-12")
                .type("Default")
                .user(defaultCreateUserDTO())
                .balance(0)
                .active(true)
                .build();
    }

    private IcesiAccount createDefaultDisabledAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-13")
                .type("Default")
                .user(defaultCreateUser())
                .balance(1000000)
                .active(false)
                .build();
    }

    private AccountCreateDTO createDefaultDTODisabledAccount(){
        return AccountCreateDTO.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-13")
                .type("Default")
                .user(defaultCreateUserDTO())
                .balance(1000000)
                .active(false)
                .build();
    }

    private IcesiAccount createDefaultNegativeBalanceAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-14")
                .type("Default")
                .user(defaultCreateUser())
                .balance(-1000000)
                .active(true)
                .build();
    }

    private AccountCreateDTO createDefaultDTONegativeBalanceAccount(){
        return AccountCreateDTO.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-14")
                .type("Default")
                .user(defaultCreateUserDTO())
                .balance(-1000000)
                .active(true)
                .build();
    }

    private IcesiUser defaultCreateUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("example@example.com")
                .password("123456")
                .phoneNumber("1234567890")
                .role(defaultCreateRole())
                .build();
    }

    private UserCreateDTO defaultCreateUserDTO(){
        return UserCreateDTO.builder()
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("example@example.com")
                .password("123456")
                .phoneNumber("1234567890")
                .role(defaultCreateRoleDTO())
                .build();
    }

    private RoleCreateDTO defaultCreateRoleDTO(){
        return RoleCreateDTO.builder()
                .roleId(UUID.randomUUID())
                .name("ROLE_USER")
                .build();
    }

    private IcesiRole defaultCreateRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ROLE_USER")
                .build();
    }
}
