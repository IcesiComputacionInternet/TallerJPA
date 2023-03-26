package com.example.jpa.service;

import com.example.jpa.repository.AccountRepository;
import com.example.jpa.repository.UserRepository;
import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.RoleDTO;
import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.mapper.AccountMapper;
import com.example.jpa.mapper.AccountMapperImpl;
import com.example.jpa.matcher.AccountMatcher;
import com.example.jpa.model.IcesiAccount;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper mapper;
    private UserRepository userRepository;

    private final String NORMAL = "NORMAL";
    private final String DEPOSIT = "DEPOSIT";

    @BeforeEach
    private void init(){
        accountRepository = mock(AccountRepository.class);
        mapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository,mapper,userRepository);
    }

    @Test
    public void testSaveAccount(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        accountService.save(defaultAccountDTO(NORMAL));
        verify(userRepository,times(1)).findByEmail(any());
        verify(mapper,times(1)).fromAccountDTO(any());
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(defaultAccount(NORMAL))));
    }

    @Test
    public void saveAccountWhenTheBalanceIsBellowZero() {
        AccountRequestDTO account = defaultAccountDTO(NORMAL);
        account.setBalance(-1L);
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            accountService.save(account);
            fail();
        }catch (Exception e){
            assertEquals("Low balance: " + account.getBalance()
                    + "/n" + "Balance can't be negative",e.getMessage());
        }
    }


    @Test
    public void saveAccountWhenTheUserDoesNotExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        try {
            accountService.save(defaultAccountDTO(NORMAL));
            fail();
        }catch (Exception e){
            assertEquals("User not found",e.getMessage());
        }
    }

    @Test
    public void disableAccount(){
        IcesiAccount account = defaultAccount(NORMAL);
        account.setBalance(0L);
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.of(account));
        assertTrue(accountService.disableAccount(anyString()));
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void disableAccountWhenBalanceIsNotZero(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));
        try {
            accountService.disableAccount(defaultAccount(NORMAL).getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account has a positive balance, it's not recommended to disable it",e.getMessage());
        }
    }

    @Test
    public void disableAccountWhenTheAccountDoesNotExist(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.disableAccount(anyString());
            fail();
        }catch (Exception e){
            assertEquals("Account not found",e.getMessage());
        }
    }

    @Test
    public void enableAccount(){
        IcesiAccount account = defaultAccount(NORMAL);
        account.setActive(false);
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.of(account));
        assertTrue(accountService.enableAccount(anyString()));
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void enableAccountWhenTheAccountNotExist(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.enableAccount(anyString());
            fail();
        }catch (Exception e){
            assertEquals("Account not found",e.getMessage());
        }
    }

    @Test
    public void enableAccountWhenTheBalanceIsNegative(){
        IcesiAccount account = defaultAccount(NORMAL);
        account.setActive(false);
        account.setBalance(-1L);
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.of(account));
        try {
            accountService.enableAccount(account.getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("Low balance: " + account.getBalance()
                    + "/n" + "Balance can't be negative",e.getMessage());
        }
    }

    //TODO: Testear los métodos de aquí para abajo
    @Test
    public void withdraw(){
        IcesiAccount icesiAccountExpected = defaultAccount(NORMAL);
        icesiAccountExpected.setBalance(icesiAccountExpected.getBalance()-1L);
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));
        assertTrue(accountService.withdraw(anyString(),1L));
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(icesiAccountExpected)));
    }

    @Test
    public void withdrawWhenTheBalanceIsLess(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));
        assertFalse(accountService.withdraw(anyString(),1L));
        verify(accountRepository,times(0)).save(any());
    }

    @Test
    public void withdrawWhenTheAccountDoesNotExist(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(accountService.withdraw(anyString(),1L));
        verify(accountRepository,times(0)).save(any());
    }

    @Test
    public void deposit(){
        IcesiAccount icesiAccountExpected = defaultAccount(NORMAL);
        icesiAccountExpected.setBalance(icesiAccountExpected.getBalance()+1L);

        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));
        assertTrue(accountService.deposit(anyString(),1L));
        verify(accountRepository,times(1)).save(argThat(new AccountMatcher(icesiAccountExpected)));
    }

    @Test
    public void depositWhenTheAccountDoesNotExist(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(accountService.deposit(anyString(), 1L));
        verify(accountRepository,times(0)).save(any());
    }

    @Test
    public void transfer(){
        IcesiAccount accountTestExpected = defaultAccount(NORMAL);
        IcesiAccount anotherAccountTestExpected = defaultAccount(NORMAL);
        anotherAccountTestExpected.setAccountNumber("233-121219-21");

        when(accountRepository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));
        when(accountRepository.getByAccountNumber("233-121219-21")).thenReturn(Optional.of(anotherAccountTestExpected));

        accountTestExpected.setBalance(accountTestExpected.getBalance()- 5L);
        anotherAccountTestExpected.setBalance(anotherAccountTestExpected.getBalance()+5L);

        assertTrue(accountService.transfer("897-887868-67","233-121219-21",5L));
        verify(accountRepository,times(2)).save(any(IcesiAccount.class));

        if(accountRepository.getByAccountNumber("897-887868-67").isPresent() && accountRepository.getByAccountNumber("233-121219-21").isPresent()){
            assertEquals(accountRepository.getByAccountNumber("897-887868-67").get().getBalance(),accountTestExpected.getBalance());
            assertEquals(accountRepository.getByAccountNumber("233-121219-21").get().getBalance(),anotherAccountTestExpected.getBalance());
        }else{
            fail();
        }
    }

    @Test
    public void transferWhenBothAccountsDoesNotExist(){
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(accountService.transfer("","",0L));
        verify(accountRepository,times(0)).save(any());
    }

    @Test
    public void transferWhenAnAccountDoesNotExist(){
        when(accountRepository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(accountService.transfer("897-887868-67",any(),0L));
        verify(accountRepository,times(0)).save(any());
    }

    @Test
    public void transferWhenAnAccountIsDeposit(){
        IcesiAccount depositTestAccount = defaultAccount(DEPOSIT);
        depositTestAccount.setAccountNumber("233-121219-21");

        when(accountRepository.getByAccountNumber("233-121219-21")).thenReturn(Optional.of(depositTestAccount));
        when(accountRepository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)));

        try {
            accountService.transfer("233-121219-21","897-887868-67",0L);
            fail();
        }catch (Exception e){
            assertEquals("One of the accounts can't perform the transaction or one of them has is type deposit",e.getMessage());
            verify(accountRepository,times(0)).save(any());
        }


    }
    @Test
    public void transferWhenBothAccountsAreDeposit(){
        IcesiAccount depositTestAccount = defaultAccount(DEPOSIT);
        depositTestAccount.setAccountNumber("233-121219-21");
        IcesiAccount anotherDepositTestAccount = defaultAccount(DEPOSIT);

        when(accountRepository.getByAccountNumber("233-121219-21")).thenReturn(Optional.of(depositTestAccount));
        when(accountRepository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(anotherDepositTestAccount));

        try {
            accountService.transfer("233-121219-21","897-887868-67",0L);
            fail();
        }catch (Exception e){
            assertEquals("One of the accounts can't perform the transaction or one of them has is type deposit",e.getMessage());
            verify(accountRepository,times(0)).save(any());
        }

    }

    @Test
    public void testTransactionWithAnInactiveAccount(){
        IcesiAccount account = defaultAccount(NORMAL);
        account.setActive(false);
        account.setAccountNumber("123-456789-11");
        when(accountRepository.getByAccountNumber("123-456789-11")).thenReturn(Optional.empty());

        assertFalse(accountService.deposit(account.getAccountNumber(),1L));
        assertFalse(accountService.withdraw(account.getAccountNumber(),1L));
        assertFalse(accountService.transfer(account.getAccountNumber(),defaultAccount(NORMAL).getAccountNumber(),1L));

        verify(accountRepository,times(0)).save(any());

    }


    @Test
    public void testUniqueAccountNumber(){
        when(userRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(accountRepository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount(NORMAL)),Optional.empty());
        accountService.save(defaultAccountDTO(NORMAL));
        verify(accountRepository,times(2)).getByAccountNumber(any());

    }


    private IcesiAccount defaultAccount(String type){
        return IcesiAccount.builder()
                .accountNumber("897-887868-67")
                .balance(10L)
                .active(true)
                .type(type)
                .user(defaultUser())
                .build();
    }

    private AccountRequestDTO defaultAccountDTO(String type){
        return AccountRequestDTO.builder()
                .balance(10L)
                .type(type)
                .user(defaultUserDTO())
                .build();
    }


    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("6ee86844-1e1e-41d1-b7e7-72471a144645"))
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("123456789")
                .email("jhon.doe@gmail.com")
                .role(defaultRole())
                .icesiAccountList(new ArrayList<>())
                .build();
    }

    private UserRequestDTO defaultUserDTO(){
        return UserRequestDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("123456789")
                .email("jhon.doe@gmail.com")
                .role(defaultRoleDTO())
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .icesiUserList(new ArrayList<>())
                .build();
    }

    private RoleDTO defaultRoleDTO(){
        return   RoleDTO.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }

}
