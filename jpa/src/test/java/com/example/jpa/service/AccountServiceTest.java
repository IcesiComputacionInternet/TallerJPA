package com.example.jpa.service;

import com.example.jpa.dto.AccountRequestDTO;
import com.example.jpa.dto.RoleDTO;
import com.example.jpa.dto.UserRequestDTO;
import com.example.jpa.mapper.AccountMapper;
import com.example.jpa.mapper.AccountMapperImpl;
import com.example.jpa.matcher.AccountMatcher;
import com.example.jpa.model.IcesiAccount;
import com.example.jpa.model.IcesiRole;
import com.example.jpa.model.IcesiUser;
import com.example.jpa.repository.AccountRepository;
import com.example.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService service;
    private AccountRepository repository;
    private AccountMapper mapper;
    private IcesiAccount account;

    private UserRepository userRepository;

    @BeforeEach
    private void init(){
        repository = mock(AccountRepository.class);
        mapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        service = new AccountService(repository,mapper,userRepository);
        account = defaultAccount("NORMAL");
    }

    @Test
    public void testSaveAccount(){
        when(userRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        service.save(defaultDTO("NORMAL"));
        verify(mapper,times(1)).fromAccountDTO(any());
        verify(userRepository,times(2)).findByName(any());
        verify(repository,times(1)).save(argThat(new AccountMatcher(defaultAccount("NORMAL"))));
        verify(userRepository,times(1)).save(any());

    }

    @Test
    public void createAccountWhenTheBalanceIsBellowZero(){
        account.setBalance(-1L);
        when(userRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(mapper.fromAccountDTO(any())).thenReturn(account);
        try {
            service.save(defaultDTO("NORMAL"));
            fail();
        }catch (Exception e){
            assertEquals("You can't create an account with balance below to  0",e.getMessage());
        }
    }

    @Test
    public void createAccountWhenTheUserDoesNotExist(){
        when(userRepository.findByName(any())).thenReturn(Optional.empty());
        try {
            service.save(defaultDTO("NORMAL"));
            fail();
        }catch (Exception e){
            assertEquals("The user doesn't exist",e.getMessage());
        }
    }

    @Test
    public void disableAccount(){
        account.setBalance(0L);
        when(repository.getByAccountNumber(any())).thenReturn(Optional.of(account));
        assertTrue(service.disableAccount(anyString()));
        verify(repository,times(2)).getByAccountNumber(any());
        verify(repository,times(1)).save(any(IcesiAccount.class));
        if(repository.getByAccountNumber(any()).isPresent()){
            assertFalse(repository.getByAccountNumber(any()).get().isActive());
        }else {
            fail();
        }
    }

    @Test
    public void disableAccountWhenBalanceIsNotZero(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertFalse(service.disableAccount(anyString()));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void disableAccountWhenTheAccountDoesNotExist(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.disableAccount(anyString()));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void enableAccount(){
        account.setActive(false);
        when(repository.getByAccountNumber(any())).thenReturn(Optional.of(account));
        assertTrue(service.enableAccount(anyString()));
        verify(repository,times(2)).getByAccountNumber(any());
        verify(repository,times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void enableAccountWhenTheAccountNotExist(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.enableAccount(anyString()));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void enableAccountWhenTheAccountIsNotDisable(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertFalse(service.enableAccount(anyString()));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void withdraw(){
        IcesiAccount icesiAccountExpected = defaultAccount("NORMAL");
        icesiAccountExpected.setBalance(icesiAccountExpected.getBalance()-1L);
        when(repository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertTrue(service.withdraw(anyString(),1L));
        verify(repository,times(1)).save(argThat(new AccountMatcher(icesiAccountExpected)));
    }

    @Test
    public void withdrawWhenTheBalanceIsLess(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertFalse(service.withdraw(anyString(),1L));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void withdrawWhenTheAccountDoesNotExist(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.withdraw(anyString(),1L));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void deposit(){
        IcesiAccount icesiAccountExpected = defaultAccount("NORMAL");
        icesiAccountExpected.setBalance(icesiAccountExpected.getBalance()+1L);

        when(repository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertTrue(service.deposit(anyString(),1L));
        verify(repository,times(1)).save(argThat(new AccountMatcher(icesiAccountExpected)));
    }

    @Test
    public void depositWhenTheAccountDoesNotExist(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.deposit(anyString(), 1L));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void transfer(){
        IcesiAccount accountTestExpected = defaultAccount("NORMAL");
        IcesiAccount anotherAccountTestExpected = defaultAccount("NORMAL");
        anotherAccountTestExpected.setAccountNumber("233-121219-21");

        when(repository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        when(repository.getByAccountNumber("233-121219-21")).thenReturn(Optional.of(anotherAccountTestExpected));

        accountTestExpected.setBalance(accountTestExpected.getBalance()- 5L);
        anotherAccountTestExpected.setBalance(anotherAccountTestExpected.getBalance()+5L);

        assertTrue(service.transfer("897-887868-67","233-121219-21",5L));
        verify(repository,times(2)).save(any(IcesiAccount.class));

        if(repository.getByAccountNumber("897-887868-67").isPresent() && repository.getByAccountNumber("233-121219-21").isPresent()){
            assertEquals(repository.getByAccountNumber("897-887868-67").get().getBalance(),accountTestExpected.getBalance());
            assertEquals(repository.getByAccountNumber("233-121219-21").get().getBalance(),anotherAccountTestExpected.getBalance());
        }else{
            fail();
        }




    }

    @Test
    public void transferWhenBothAccountsDoesNotExist(){
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.transfer("","",0L));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void transferWhenAnAccountDoesNotExist(){
        when(repository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        when(repository.getByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.transfer("897-887868-67",any(),0L));
        verify(repository,times(0)).save(any());
    }

    @Test
    public void transferWhenAnAccountIsDeposit(){
        IcesiAccount depositTestAccount = defaultAccount("deposit");
        depositTestAccount.setAccountNumber("233-121219-21");

        when(repository.getByAccountNumber("233-121219-21")).thenReturn(Optional.of(depositTestAccount));
        when(repository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));

        try {
            service.transfer("233-121219-21","897-887868-67",0L);
            fail();
        }catch (Exception e){
            assertEquals("One of the accounts can't perform the transaction or one of them has is type deposit",e.getMessage());
            verify(repository,times(0)).save(any());
        }


    }
    @Test
    public void transferWhenBothAccountsAreDeposit(){
        IcesiAccount depositTestAccount = defaultAccount("deposit");
        depositTestAccount.setAccountNumber("233-121219-21");
        IcesiAccount anotherDepositTestAccount = defaultAccount("deposit");

        when(repository.getByAccountNumber("233-121219-21")).thenReturn(Optional.of(depositTestAccount));
        when(repository.getByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(anotherDepositTestAccount));

        try {
            service.transfer("233-121219-21","897-887868-67",0L);
            fail();
        }catch (Exception e){
            assertEquals("One of the accounts can't perform the transaction or one of them has is type deposit",e.getMessage());
            verify(repository,times(0)).save(any());
        }

    }

    @Test
    public void testTransactionWithAnInactiveAccount(){
        account.setActive(false);
        account.setAccountNumber("123-456789-11");
        when(repository.getByAccountNumber("123-456789-11")).thenReturn(Optional.empty());

        assertFalse(service.deposit(account.getAccountNumber(),1L));
        assertFalse(service.withdraw(account.getAccountNumber(),1L));
        assertFalse(service.transfer(account.getAccountNumber(),defaultAccount("NORMAL").getAccountNumber(),1L));

        verify(repository,times(0)).save(any());

    }


    @Test
    public void testUniqueAccountNumber(){
        when(userRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(repository.getByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("normal")),Optional.empty());
        service.save(defaultDTO("NORMAL"));
        verify(repository,times(2)).getByAccountNumber(any());

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

    private AccountRequestDTO defaultDTO(String type){
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
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(defaultRole())
                .icesiAccountList(new ArrayList<>())
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .icesiUserList(new ArrayList<>())
                .build();
    }

    private UserRequestDTO defaultUserDTO(){
        return UserRequestDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(defaultRoleDTO())
                .build();
    }

    private RoleDTO defaultRoleDTO(){
        return   RoleDTO.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }

}
