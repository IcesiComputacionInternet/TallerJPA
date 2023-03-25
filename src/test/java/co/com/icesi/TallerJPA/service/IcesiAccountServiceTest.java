package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.*;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJPA.mapper.IcesiAccountMapperImpl;
import co.com.icesi.TallerJPA.matcher.IcesiAccountMatcher;
import co.com.icesi.TallerJPA.matcher.IcesiUserMatcher;
import co.com.icesi.TallerJPA.model.IcesiAccount;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiAccountRespository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiAccountServiceTest {
    private IcesiAccountService accountService;
    private IcesiAccountRespository accountRespository;
    private IcesiAccountMapper mapper;

    private IcesiUserRepository userRepository;

    @BeforeEach()
    public void init(){
        accountRespository = mock(IcesiAccountRespository.class);
        mapper = spy(IcesiAccountMapperImpl.class);
        userRepository = mock(IcesiUserRepository.class);
        accountService = new IcesiAccountService(mapper, accountRespository,userRepository);

    }

    @Test
    public void createAccount(){
        when(userRepository.findbyName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        accountService.save(defaultDTO("NORMAL"));
        verify(mapper,times(1)).fromAccountDTO(any());
        verify(userRepository,times(1)).findbyName(any());
        verify(accountRespository,times(1)).save(argThat(new IcesiAccountMatcher(defaultAccount("NORMAL"))));
        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(defaultUser())));

    }

    @Test
    public void createAccountWhenTheBalanceIsBellowZero(){
       IcesiAccountDTO accountDTO = defaultDTO("NORMAL");
        accountDTO.setBalance(-1L);
        when(userRepository.findbyName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            accountService.save(accountDTO);
            fail();
        }catch (Exception e){
            assertEquals("You can't create an account with balance below to  0",e.getMessage());
        }
    }

    @Test
    public void createAccountWhenTheUserDoesNotExist(){
        when(userRepository.findbyName(any())).thenReturn(Optional.empty());
        try {
            accountService.save(defaultDTO("NORMAL"));
            fail();
        }catch (Exception e){
            assertEquals("The user doesn't exist",e.getMessage());
        }
    }

    @Test
    public void disableAccount(){
        IcesiAccount accountToDesable = defaultAccount("NORMAL");
        accountToDesable.setBalance(0L);
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.of(accountToDesable));
        assertEquals("The account was disabled", accountService.disableAccount(any()));
        verify(accountRespository,times(1)).save(argThat(new IcesiAccountMatcher(accountToDesable)));

    }

    @Test
    public void disableAccountWhenBalanceIsNotZero(){
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("NORMAL")));
        try{
            accountService.disableAccount(defaultAccount("NORMAL").getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account "+defaultAccount("NORMAL").getAccountNumber()+" can not be disabled",e.getMessage());
        }
    }

    @Test
    public void disableAccountWhenTheAccountDoesNotExist(){
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try{
            accountService.disableAccount(defaultAccount("NORMAL").getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account: "+defaultAccount("NORMAL").getAccountNumber()+" was not found",e.getMessage());
        }
    }

    @Test
    public void enableAccount(){
        IcesiAccount accountToEnable = defaultAccount("NORMAL");
        accountToEnable.setActive(false);
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.of(accountToEnable));
        assertEquals("The account was enabled",accountService.enableAccount(accountToEnable.getAccountNumber()));
        verify(accountRespository, times(1)).save(argThat(new IcesiAccountMatcher(accountToEnable)));
    }

    @Test
    public void enableAccountWhenTheAccountNotExist(){
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.enableAccount(defaultAccount("NORMAL").getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("The account: "+ defaultAccount("NORMAL").getAccountNumber() +" was not found",e.getMessage());
        }
    }

    @Test
    public void enableAccountWhenTheAccountIsNotDisabled(){
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.of(defaultAccount("NORMAL")));
        try {
            accountService.enableAccount(defaultAccount("NORMAL").getAccountNumber());
            fail();
        }catch (Exception e){
            assertEquals("This account was enabled already",e.getMessage());
        }
    }

    @Test
    public void withdrawalMoney(){
        IcesiAccount accountTest = defaultAccount("NORMAL");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(accountTest.getAccountNumber())
                .amount(5L)
                .build();
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.of(accountTest));
        accountService.withdrawalMoney(transactionDTO);
        assertEquals(5L,accountTest.getBalance());
        verify(accountRespository,times(1)).save(argThat(new IcesiAccountMatcher(accountTest)));


    }

    @Test
    public void withdrawalMoneyWhenTheBalanceIsLess(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(defaultAccount("NORMAL").getAccountNumber())
                .amount(12L)
                .build();

        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.of(defaultAccount("NORMAL")));
        try {
            accountService.withdrawalMoney(transactionDTO);
            fail();
        }catch (Exception e){
            assertEquals("The balance is Less your current balance is" + defaultAccount("NORMAL").getBalance(),e.getMessage());
        }
    }

    @Test
    public void withdrawalMoneyWhenTheAccountDoesNotExist(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(defaultAccount("NORMAL").getAccountNumber())
                .amount(12L)
                .build();
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.withdrawalMoney(transactionDTO);
            fail();
        }catch (Exception e){
            assertEquals("The account: "+ defaultAccount("NORMAL").getAccountNumber() +" was not found",e.getMessage());
        }
    }

    @Test
    public void depositMoney(){
        IcesiAccount accountTest = defaultAccount("NORMAL");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(accountTest.getAccountNumber())
                .amount(5L)
                .build();
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.of(accountTest));
        accountService.depositMoney(transactionDTO);
        assertEquals(15L,accountTest.getBalance());
        verify(accountRespository,times(1)).save(argThat(new IcesiAccountMatcher(accountTest)));
    }

    @Test
    public void depositMoneyWhenTheAccountDoesNotExist(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(defaultAccount("NORMAL").getAccountNumber())
                .amount(12L)
                .build();
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.empty());
        try {
            accountService.depositMoney(transactionDTO);
            fail();
        }catch (Exception e){
            assertEquals("The account: "+ defaultAccount("NORMAL").getAccountNumber() +" was not found",e.getMessage());
        }
    }

    @Test

    public void transferMoney(){
        IcesiAccount accountSource = defaultAccount("NORMAL");
        IcesiAccount accountDestination =defaultAccount("NORMAL");
        accountDestination.setAccountNumber("232-123132-24");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(defaultAccount("NORMAL").getAccountNumber())
                .accountNumberTo(accountDestination.getAccountNumber())
                .amount(5L)
                .build();
        when(accountRespository.findByAccountNumber("897-887868-67")).thenReturn(Optional.of(accountSource));
        when(accountRespository.findByAccountNumber("232-123132-24")).thenReturn(Optional.of(accountDestination));

        accountService.transferMoney(transactionDTO);

        assertEquals(5L,accountSource.getBalance());
        assertEquals(15L, accountDestination.getBalance());

        verify(accountRespository,times(1)).save(argThat(new IcesiAccountMatcher(accountSource)));
        verify(accountRespository,times(1)).save(argThat(new IcesiAccountMatcher(accountDestination)));

    }

    @Test
    public void transferMoneyWhenBothAccountsDoNotExist(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder().build();
       when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.empty(),Optional.empty());
       assertThrows(RuntimeException.class, () -> accountService.transferMoney(transactionDTO));
       verify(accountRespository,times(0)).save(any());

    }

    @Test
    public void transferMoneyWhenAnAccountDoesNotExist(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(defaultAccount("NORMAL").getAccountNumber())
                .accountNumberTo("121-121212-12")
                .amount(5L)
                .build();
        when(accountRespository.findByAccountNumber("897-887868-67")).thenReturn(Optional.of(defaultAccount("NORMAL")));
        when(accountRespository.findByAccountNumber("121-121212-12")).thenReturn(Optional.empty());

        try {
            accountService.transferMoney(transactionDTO);
            fail();
        }catch (Exception e){
            assertEquals("The account: 121-121212-12 was not found", e.getMessage());
        }


    }

    @Test
    public void transferMoneyWhenAnAccountIsDeposit(){
        IcesiAccount accountSource = defaultAccount("NORMAL");
        IcesiAccount accountDestination =defaultAccount("deposit");
        accountDestination.setAccountNumber("137-123332-74");
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder()
                .accountNumberFrom(defaultAccount("NORMAL").getAccountNumber())
                .accountNumberTo(accountDestination.getAccountNumber())
                .amount(5L)
                .build();

        when(accountRespository.findByAccountNumber("897-887868-67")).thenReturn(Optional.of(accountSource));
        when(accountRespository.findByAccountNumber("137-123332-74")).thenReturn(Optional.of(accountDestination));

        try {
            accountService.transferMoney(transactionDTO);
        }catch (Exception e){
            assertEquals("The account: "+accountDestination.getAccountNumber()+" is type deposit", e.getMessage());
        }


    }

    @Test
    public void testAnyTransactionWithAnInactiveAccount(){
        TransactionRequestDTO transactionDTO = TransactionRequestDTO.builder().build();
        IcesiAccount account = defaultAccount("NORMAL");
        account.setActive(false);
        account.setAccountNumber("123-456789-11");
        when(accountRespository.findByAccountNumber("123-456789-11")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.depositMoney(transactionDTO));
        assertThrows(RuntimeException.class, () -> accountService.withdrawalMoney(transactionDTO));
        assertThrows(RuntimeException.class, () -> accountService.transferMoney(transactionDTO));

        verify(accountRespository,times(0)).save(any());

    }


    @Test
    public void testUniqueAccountNumber(){
        when(userRepository.findbyName(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(accountRespository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(defaultAccount("normal")),Optional.empty());
        accountService.save(defaultDTO("NORMAL"));
        verify(accountRespository,times(2)).findByAccountNumber(any());

    }

    @Test
    public void testAccountNumberMatchPattern(){
        String pattern = "^[0-9]{3}-[0-9]{6}-[0-9]{2}$";
        String accountNumberGenerated = generateRamdomAccountNumber();
        assertTrue(accountNumberGenerated.matches(pattern)
                && accountNumberGenerated.chars().filter(c -> c!='-').allMatch(Character::isDigit));

    }


    private String generateRamdomAccountNumber(){
        //This is the same way the account numbers are generated at the service
        Random random = new Random();
        String accountNumber = IntStream.range(0,11)
                .mapToObj(x -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
        accountNumber = accountNumber.substring(0,3) + "-" + accountNumber.substring(3,9)+"-"+accountNumber.substring(9,11);
        return accountNumber;
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
