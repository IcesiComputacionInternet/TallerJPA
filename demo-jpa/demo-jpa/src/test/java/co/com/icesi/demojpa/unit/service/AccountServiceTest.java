package co.com.icesi.demojpa.unit.service;


import co.com.icesi.demojpa.dto.request.AccountCreateDTO;
import co.com.icesi.demojpa.enums.IcesiAccountType;
import co.com.icesi.demojpa.error.exception.IcesiException;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.AccountMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AccountServiceTest {


    private AccountMapper accountMapper;
    private AccountRepository accountRepository;
    private UserRepository userRespository;
    private AccountService accountService;


    @BeforeEach
    public void setup() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        accountRepository = mock(AccountRepository.class);
        userRespository = mock(UserRepository.class);
        accountMapper = spy(AccountMapperImpl.class);

        accountService = new AccountService(accountMapper, accountRepository, userRespository);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private AccountCreateDTO defaultAccountDTO() {
        return AccountCreateDTO.builder()
                .balance(1000000L)
                .type(IcesiAccountType.DEPOSIT)
                .email("example@mail.com")
                .build();
    }

    private IcesiAccount defaultAccount() {
        return IcesiAccount.builder()
                .accountNumber("123456789")
                .balance(1000L)
                .type(IcesiAccountType.DEPOSIT)
                .isActive(true)
                .user(defaultUser())
                .build();

    }

    private IcesiUser defaultUser() {
        return IcesiUser.builder()
                .userId(UUID.fromString("d6391e20-8c56-4378-9e72-013c6f2f6f88"))
                .firstName("Juan")
                .lastName("Perez")
                .email("example@mail.com")
                .phoneNumber("123456789")
                .password("123456")
                .role(defaultRole())
                .build();
    }

    private IcesiRole defaultRole() {
        return IcesiRole.builder()
                .name("USER")
                .description("User role")
                .build();
    }

    @Test
    public void testSaveAccount() {

        when(userRespository.findByEmail(any())).thenReturn(Optional.of(defaultUser()));

        IcesiAccount icesiAccount = defaultAccount();
        AccountCreateDTO accountDTO = defaultAccountDTO();

        when(accountMapper.fromAccountCreateDTO(accountDTO)).thenReturn(icesiAccount);
        accountService.save(accountDTO);
        verify(accountMapper, times(1)).fromAccountCreateDTO(accountDTO);
        verify(accountRepository, times(1)).save(icesiAccount);

    }

    @Test
    public void testSaveAccountWithUserNotFound() {
        when(userRespository.findByEmail(any())).thenReturn(Optional.empty());

        try {
            accountService.save(defaultAccountDTO());
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("User not found", message);
        }
    }

    @Test
    public void testSaveAccountWithBalanceLessTo0() {
        AccountCreateDTO icesiAccountDTO = defaultAccountDTO();
        icesiAccountDTO.setBalance(-1L);

        try {
            accountService.save(icesiAccountDTO);
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Balance can't be below 0", message);
        }
    }

    @Test
    public void testGenerateAccountNumber() {
        String accountNumber = accountService.generateAccountNumber();
        assertEquals(13, accountNumber.length());
        assertTrue(accountNumber.matches("^\\d{3}-\\d{6}-\\d{2}$"));
    }

    @Test
    public void testSetAccountNumberGenerate(){

        String number = accountService.generateAccountNumber();
        when(accountRepository.existsByAccountNumber(number)).thenReturn(false);

        IcesiAccount icesiAccount = defaultAccount();
        icesiAccount.setAccountNumber(number);
        assertNotNull(icesiAccount.getAccountNumber());
        assertEquals(number, icesiAccount.getAccountNumber());
    }

    @Test
    public void testSetTypeAccountDeposit(){
        IcesiAccount icesiAccount = defaultAccount();
        accountService.setTypeAccount("DEPOSIT", icesiAccount);

        assertNotNull(icesiAccount.getType());
        assertEquals(IcesiAccountType.DEPOSIT, icesiAccount.getType());
    }

    @Test
    public void testSetTypeAccountDefault(){
        IcesiAccount icesiAccount = defaultAccount();
        accountService.setTypeAccount("DEFAULT", icesiAccount);

        assertNotNull(icesiAccount.getType());
        assertEquals(IcesiAccountType.DEFAULT, icesiAccount.getType());
    }

    @Test
    public void testSetTypeAccountWhenTypeDoesNotExist(){
        IcesiAccount icesiAccount = defaultAccount();
        try {
            accountService.setTypeAccount("DEPOSIT20", icesiAccount);
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Account type does not exist", message);
        }
    }

    @Test
    public void testActivateAccount(){
        try {
            IcesiAccount icesiAccount = defaultAccount();
            String msg = accountService.activeAccount(icesiAccount.getAccountNumber());

            verify(accountRepository, times(1)).activeAccount(icesiAccount.getAccountNumber());
            assertEquals(msg, "The account " + icesiAccount.getAccountNumber() +" was active");
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("User not found", message);
        }


    }

    @Test
    public void testDisableAccount() {

        try {
            IcesiAccount icesiAccount = defaultAccount();
            icesiAccount.setBalance(0L);
            String msg = accountService.disableAccount(icesiAccount.getAccountNumber());

            verify(accountRepository, times(1)).inactiveAccount(icesiAccount.getAccountNumber());
            assertEquals(msg, "The account " + icesiAccount.getAccountNumber() + " was inactive");
        } catch (IcesiException exception) {
            String message = exception.getMessage();
            assertEquals("User not found", message);
        }
    }



    @Test
    public void testIcesiAccountByActive(){
        IcesiAccount icesiAccount = defaultAccount();
        when(accountRepository.IcesiAccountByActive(icesiAccount.getAccountNumber())).thenReturn(true);
        accountRepository.IcesiAccountByActive(icesiAccount.getAccountNumber());
        verify(accountRepository, times(1)).IcesiAccountByActive(icesiAccount.getAccountNumber());
    }


    @Test
    public void testDisableAccountWithBalanceDifferentTo0(){
        IcesiAccount icesiAccount = defaultAccount();
        icesiAccount.setBalance(1000L);
        try{
            doNothing().when(accountRepository).inactiveAccount(icesiAccount.getAccountNumber());
            when(accountRepository.IcesiAccountByActive(icesiAccount.getAccountNumber())).thenReturn(true);
            accountService.disableAccount(icesiAccount.getAccountNumber());
            verify(accountRepository, times(1)).inactiveAccount(icesiAccount.getAccountNumber());
            verify(accountRepository, times(1)).IcesiAccountByActive(icesiAccount.getAccountNumber());
        } catch (RuntimeException e) {
            String message = e.getMessage();
            assertEquals("User not found", message);
        }
    }

    @Test
    public void testWithdrawAccount(){
        IcesiAccount icesiAccount = defaultAccount();
        icesiAccount.setBalance(1000L);

        String msg = accountService.withdrawal(icesiAccount.getAccountNumber(), 100L);
        verify(accountRepository, times(1)).withdrawalAccount(icesiAccount.getAccountNumber(), 100L);

        assertEquals(msg, "The withdrawal was successful");
    }


    @Test
    public void testWithdrawAccountWithBalanceLessThanAmount(){
        IcesiAccount icesiAccount = defaultAccount();
        try {
            doThrow(DataIntegrityViolationException.class)
                    .when(accountRepository).withdrawalAccount(icesiAccount.getAccountNumber(), 100000000L);
            accountService.withdrawal(icesiAccount.getAccountNumber(), 100000000L);
            verify(accountRepository, times(1)).withdrawalAccount(icesiAccount.getAccountNumber(), 100000000L);
        } catch (IcesiException e){
            String message = e.getMessage();
            assertEquals("The withdrawal can't be done", message);
        }
    }




    @Test
    public void testDeposit(){
        IcesiAccount icesiAccount = defaultAccount();
        String msg = accountService.deposit(icesiAccount.getAccountNumber(), 100L);
        verify(accountRepository, times(1)).depositAccount(icesiAccount.getAccountNumber(), 100L);
        assertEquals(msg, "Deposit was successful");
    }

    @Test
    public void testDepositWithAmountNegative(){
        IcesiAccount icesiAccount = defaultAccount();
        try {
            accountService.deposit(icesiAccount.getAccountNumber(), -100L);
            verify(accountRepository, times(1)).depositAccount(icesiAccount.getAccountNumber(), -100L);
        } catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("The value don't be negative", message);
        }
    }


    @Test
    public void testTransfer(){
        IcesiAccount icesiAccountOrigin = defaultAccount();
        icesiAccountOrigin.setType(IcesiAccountType.valueOf("DEFAULT"));
        IcesiAccount icesiAccountDestination = defaultAccount();
        icesiAccountDestination.setAccountNumber("123-123456-12");
        icesiAccountDestination.setType(IcesiAccountType.valueOf("DEFAULT"));


        when(accountRepository.getTypeofAccount(icesiAccountOrigin.getAccountNumber())).thenReturn(Optional.of(icesiAccountOrigin));
        when(accountRepository.getTypeofAccount(icesiAccountDestination.getAccountNumber())).thenReturn(Optional.of(icesiAccountDestination));
        doNothing().when(accountRepository).withdrawalAccount(icesiAccountOrigin.getAccountNumber(), 100L);
        doNothing().when(accountRepository).depositAccount(icesiAccountDestination.getAccountNumber(), 100L);


        String msg = accountService.transfer(icesiAccountOrigin.getAccountNumber(), icesiAccountDestination.getAccountNumber(), 100L);

        verify(accountRepository, times(1)).getTypeofAccount(icesiAccountOrigin.getAccountNumber());
        verify(accountRepository, times(1)).getTypeofAccount(icesiAccountDestination.getAccountNumber());
        verify(accountRepository, times(1)).withdrawalAccount(icesiAccountOrigin.getAccountNumber(), 100L);
        verify(accountRepository, times(1)).depositAccount(icesiAccountDestination.getAccountNumber(), 100L);

        assertEquals("The transaction was successful", msg);

        /*
                IcesiAccount icesiAccount = defaultAccount();
        try {
            accountService.setTypeAccount("DEPOSIT20", icesiAccount);
            fail();
        } catch (RuntimeException exception) {
            String message = exception.getMessage();
            assertEquals("Account type does not exist", message);
        }
         */
    }

    @Test
    public void testTransferWithOriginDeposit(){
        IcesiAccount icesiAccountOrigin = defaultAccount();
        IcesiAccount icesiAccountDestination = defaultAccount();
        icesiAccountDestination.setAccountNumber("123-123456-12");
        icesiAccountDestination.setType(IcesiAccountType.valueOf("DEFAULT"));

        try {
            when(accountRepository.getTypeofAccount(icesiAccountOrigin.getAccountNumber())).thenReturn(Optional.of(icesiAccountOrigin));
            accountService.transfer(icesiAccountOrigin.getAccountNumber(), icesiAccountDestination.getAccountNumber(), 100L);
            verify(accountRepository, times(1)).getTypeofAccount(icesiAccountOrigin.getAccountNumber());
            fail();
        } catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("Deposit Destination Account inactive", message);
        }
    }

    @Test
    public void testTransferWithDestinationDeposit(){
        IcesiAccount icesiAccountOrigin = defaultAccount();
        icesiAccountOrigin.setType(IcesiAccountType.valueOf("DEFAULT"));
        IcesiAccount icesiAccountDestination = defaultAccount();
        icesiAccountDestination.setAccountNumber("123-123456-12");

        try {
            when(accountRepository.getTypeofAccount(icesiAccountDestination.getAccountNumber())).thenReturn(Optional.of(icesiAccountDestination));
            accountService.transfer(icesiAccountOrigin.getAccountNumber(), icesiAccountDestination.getAccountNumber(), 100L);
            verify(accountRepository, times(1)).getTypeofAccount(icesiAccountDestination.getAccountNumber());
            fail();
        } catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("Deposit Origin Account inactive", message);
        }
    }

    @Test
    public void testTransferWithErrorinWithdrawal(){
        IcesiAccount icesiAccountOrigin = defaultAccount();
        icesiAccountOrigin.setType(IcesiAccountType.valueOf("DEFAULT"));
        IcesiAccount icesiAccountDestination = defaultAccount();
        icesiAccountDestination.setAccountNumber("123-123456-12");
        icesiAccountDestination.setType(IcesiAccountType.valueOf("DEFAULT"));

        try {
            when(accountRepository.getTypeofAccount(icesiAccountOrigin.getAccountNumber())).thenReturn(Optional.of(icesiAccountOrigin));
            when(accountRepository.getTypeofAccount(icesiAccountDestination.getAccountNumber())).thenReturn(Optional.of(icesiAccountDestination));
            doThrow(DataIntegrityViolationException.class).when(accountRepository).withdrawalAccount(icesiAccountOrigin.getAccountNumber(), 100L);
            accountService.transfer(icesiAccountOrigin.getAccountNumber(), icesiAccountDestination.getAccountNumber(), 100L);
            verify(accountRepository, times(1)).getTypeofAccount(icesiAccountOrigin.getAccountNumber());
            verify(accountRepository, times(1)).getTypeofAccount(icesiAccountDestination.getAccountNumber());
            verify(accountRepository, times(1)).withdrawalAccount(icesiAccountOrigin.getAccountNumber(), 100L);
            fail();
        } catch (IcesiException e){
            String message = e.getMessage();
            assertEquals("The withdrawal can't be done", message);
        }
    }

    @Test
    public void testTransferWithErrorInDeposit(){
        IcesiAccount icesiAccountOrigin = defaultAccount();
        icesiAccountOrigin.setType(IcesiAccountType.valueOf("DEFAULT"));
        IcesiAccount icesiAccountDestination = defaultAccount();
        icesiAccountDestination.setAccountNumber("123-123456-12");
        icesiAccountDestination.setType(IcesiAccountType.valueOf("DEFAULT"));

        try {
            when(accountRepository.getTypeofAccount(icesiAccountOrigin.getAccountNumber())).thenReturn(Optional.of(icesiAccountOrigin));
            when(accountRepository.getTypeofAccount(icesiAccountDestination.getAccountNumber())).thenReturn(Optional.of(icesiAccountDestination));
            doNothing().when(accountRepository).depositAccount(icesiAccountOrigin.getAccountNumber(), -100L);
            accountService.transfer(icesiAccountOrigin.getAccountNumber(), icesiAccountDestination.getAccountNumber(), -100L);
            verify(accountRepository, times(1)).getTypeofAccount(icesiAccountOrigin.getAccountNumber());
            verify(accountRepository, times(1)).getTypeofAccount(icesiAccountDestination.getAccountNumber());
            verify(accountRepository, times(1)).depositAccount(icesiAccountDestination.getAccountNumber(), 100L);
            fail();
        } catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("The value don't be negative", message);
        }
    }
}