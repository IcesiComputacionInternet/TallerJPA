package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapperImpl;
import co.com.icesi.TallerJPA.matcher.IcesiAccountMatcher;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {
    private IcesiAccountService service;
    private IcesiAccountRespository respository;
    private IcesiAccountMapper mapper;
    private IcesiAccount account;

    private IcesiUserRepository userRepository;

    @BeforeEach
    private void init(){
        respository = mock(IcesiAccountRespository.class);
        mapper = spy(IcesiAccountMapperImpl.class);
        userRepository = mock(IcesiUserRepository.class);
        service = new IcesiAccountService(mapper,respository,userRepository);
        account = defaultAccount("NORMAL");
    }

    @Test
    public void createAccount(){
        when(userRepository.findbyName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        service.save(defaultDTO("NORMAL"));
        verify(mapper,times(1)).fromAccountDTO(any());
        verify(userRepository,times(2)).findbyName(any());
        verify(respository,times(1)).save(argThat(new IcesiAccountMatcher(defaultAccount("NORMAL"))));
        verify(userRepository,times(1)).save(any());

    }

    @Test
    public void createAccountWhenTheBalanceIsBellowZero(){
        account.setBalance(-1L);
        when(userRepository.findbyName(any())).thenReturn(Optional.ofNullable(defaultUser()));
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
        when(userRepository.findbyName(any())).thenReturn(Optional.empty());
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
        when(respository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        assertTrue(service.manageAccount(anyString(),"disable"));
        verify(respository,times(2)).findByAccountNumber(any());
        verify(respository,times(1)).save(any(IcesiAccount.class));
        if(respository.findByAccountNumber(any()).isPresent()){
           assertFalse(respository.findByAccountNumber(any()).get().isActive());
        }else {
            fail();
        }
    }

    @Test
    public void disableAccountWhenBalanceIsNotZero(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertFalse(service.manageAccount(anyString(),"disable"));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void disableAccountWhenTheAccountDoesNotExist(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.manageAccount(anyString(),"disable"));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void enableAccount(){
        account.setActive(false);
        when(respository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        assertTrue(service.manageAccount(anyString(),"enable"));
        verify(respository,times(2)).findByAccountNumber(any());
        verify(respository,times(1)).save(argThat(new IcesiAccountMatcher(account)));
    }

    @Test
    public void enableAccountWhenTheAccountNotExist(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.manageAccount(anyString(),"enable"));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void enableAccountWhenTheAccountIsNotDisable(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertFalse(service.manageAccount(anyString(),"enable"));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void withdrawalMoney(){
        IcesiAccount icesiAccountExpected = defaultAccount("NORMAL");
        icesiAccountExpected.setBalance(icesiAccountExpected.getBalance()-1L);
        when(respository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertTrue(service.withdrawalMoney(anyString(),1L));
        verify(respository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccountExpected)));
    }

    @Test
    public void withdrawalMoneyWhenTheBalanceIsLess(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertFalse(service.withdrawalMoney(anyString(),12L));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void withdrawalMoneyWhenTheAccountDoesNotExist(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.withdrawalMoney(anyString(),1L));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void depositMoney(){
        IcesiAccount icesiAccountExpected = defaultAccount("NORMAL");
        icesiAccountExpected.setBalance(icesiAccountExpected.getBalance()+1L);

        when(respository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        assertTrue(service.depositMoney(anyString(),1L));
        verify(respository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccountExpected)));
    }

    @Test
    public void depositMoneyWhenTheAccountDoesNotExist(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.depositMoney(anyString(), 1L));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void transferMoney(){
        IcesiAccount accountTestExpected = defaultAccount("NORMAL");
        IcesiAccount anotherAccountTestExpected = defaultAccount("NORMAL");
        anotherAccountTestExpected.setAccountNumber("233-121219-21");

        when(respository.findByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        when(respository.findByAccountNumber("233-121219-21")).thenReturn(Optional.of(anotherAccountTestExpected));

        accountTestExpected.setBalance(accountTestExpected.getBalance()- 5L);
        anotherAccountTestExpected.setBalance(anotherAccountTestExpected.getBalance()+5L);

        assertTrue(service.transferMoney("897-887868-67","233-121219-21",5L));
        verify(respository,times(2)).save(any(IcesiAccount.class));

        if(respository.findByAccountNumber("897-887868-67").isPresent() && respository.findByAccountNumber("233-121219-21").isPresent()){
            assertEquals(respository.findByAccountNumber("897-887868-67").get().getBalance(),accountTestExpected.getBalance());
            assertEquals(respository.findByAccountNumber("233-121219-21").get().getBalance(),anotherAccountTestExpected.getBalance());
        }else{
            fail();
        }




    }

    @Test
    public void transferMoneyWhenBothAccountsDoesNotExist(){
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.transferMoney("","",0L));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void transferMoneyWhenAnAccountDoesNotExist(){
        when(respository.findByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        when(respository.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertFalse(service.transferMoney("897-887868-67",any(),0L));
        verify(respository,times(0)).save(any());
    }

    @Test
    public void transferMoneyWhenAnAccountIsDeposit(){
        IcesiAccount depositTestAccount = defaultAccount("deposit");
        depositTestAccount.setAccountNumber("233-121219-21");

        when(respository.findByAccountNumber("233-121219-21")).thenReturn(Optional.of(depositTestAccount));
        when(respository.findByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));

        try {
            service.transferMoney("233-121219-21","897-887868-67",0L);
            fail();
        }catch (Exception e){
            assertEquals("One of the accounts can't perform the transaction or one of them has is type deposit",e.getMessage());
            verify(respository,times(0)).save(any());
        }


    }
    @Test
    public void transferMoneyWhenBothAccountsAreDeposit(){
        IcesiAccount depositTestAccount = defaultAccount("deposit");
        depositTestAccount.setAccountNumber("233-121219-21");
        IcesiAccount anotherDepositTestAccount = defaultAccount("deposit");

        when(respository.findByAccountNumber("233-121219-21")).thenReturn(Optional.of(depositTestAccount));
        when(respository.findByAccountNumber("897-887868-67")).thenReturn(Optional.ofNullable(anotherDepositTestAccount));

        try {
            service.transferMoney("233-121219-21","897-887868-67",0L);
            fail();
        }catch (Exception e){
            assertEquals("One of the accounts can't perform the transaction or one of them has is type deposit",e.getMessage());
            verify(respository,times(0)).save(any());
        }

    }

    @Test
    public void testTransactionWithAnInactiveAccount(){
        account.setActive(false);
        account.setAccountNumber("123-456789-11");
        when(respository.findByAccountNumber("123-456789-11")).thenReturn(Optional.empty());

        assertFalse(service.depositMoney(account.getAccountNumber(),1L));
        assertFalse(service.withdrawalMoney(account.getAccountNumber(),1L));
        assertFalse(service.transferMoney(account.getAccountNumber(),defaultAccount("NORMAL").getAccountNumber(),1L));

        verify(respository,times(0)).save(any());

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

    private IcesiAccountDTO defaultDTO(String type){
        return IcesiAccountDTO.builder()
                .balance(10L)
                .type(type)
                .user(defaultUserDTO())
                .build();
    }


    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .userID(UUID.fromString("6ee86844-1e1e-41d1-b7e7-72471a144645"))
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(defaultRole())
                .accounts(new ArrayList<>())
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .users(new ArrayList<>())
                .build();
    }

    private IcesiUserDTO defaultUserDTO(){
        return IcesiUserDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(defaultRoleDTO())
                .build();
    }

    private IcesiRoleDTO defaultRoleDTO(){
        return   IcesiRoleDTO.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }

}
