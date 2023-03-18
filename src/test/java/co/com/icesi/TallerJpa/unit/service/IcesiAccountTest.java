package co.com.icesi.TallerJpa.unit.service;

import co.com.icesi.TallerJpa.dto.IcesiAccountCreateDTO;
import co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions.AccountCantBeDisableException;
import co.com.icesi.TallerJpa.exceptions.icesiAccountExceptions.*;
import co.com.icesi.TallerJpa.mapper.IcesiAccountMapper;
import co.com.icesi.TallerJpa.mapper.IcesiAccountMapperImpl;
import co.com.icesi.TallerJpa.model.IcesiAccount;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiAccountRepository;
import co.com.icesi.TallerJpa.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
public class IcesiAccountTest {
    private IcesiAccountService icesiAccountService;
    private IcesiAccountRepository icesiAccountRepository;
    private IcesiAccountMapper icesiAccountMapper;
    @BeforeEach
    public void init(){
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapperImpl.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper);
    }
    @Test
    public void testCreateAccount(){
        icesiAccountService.saveAccount(defaultAccountCreateDTO(),defaultIcesiUser());
        IcesiAccount icesiAccount = IcesiAccount.builder()
                .balance(0)
                .type("Normal")
                .active(true)
                .build();
        IcesiUser icesiUser = IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@test.com")
                .phoneNumber("3201112222")
                .password("12345")
                .build();
        icesiAccount.setIcesiUser(icesiUser);
        verify(icesiAccountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }
    @Test
    public void testAccountCreateBalanceError(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try{
            icesiAccountService.saveAccount(balanceErrorAccountCreateDTO(),defaultIcesiUser());
            fail();
        }catch (AccountBalanceNotValidException exception){
            String message = exception.getMessage();
            assertEquals("AccountBalanceNotValidException: No se puede crear una cuenta con balance menor a 0", message);
        }
    }
    @Test
    public void testAccountCreateAccountNumberInUse(){
        when(icesiAccountRepository.findByAccountNumber(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try{
            icesiAccountService.saveAccount(defaultAccountCreateDTO(),defaultIcesiUser());
            fail();
        }catch (AccountNumberAlreadyInUseException exception){
            String message = exception.getMessage();
            assertEquals("AccountNumberAlreadyInUseException: The account number is already in use",message);
        }
    }
    @Test
    public void testAccountCreateTypeNotExists(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try {
            icesiAccountService.saveAccount(typeErrorAccountCreateDTO(),defaultIcesiUser());
            fail();
        }catch (AccountTypeNotExistsException exception){
            String message = exception.getMessage();
            assertEquals("AccountTypeNotExistsException: norm not exists", message);
        }
    }
    @Test
    public void testAccountCreateUserIsNull(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        try {
            icesiAccountService.saveAccount(defaultAccountCreateDTO(),null);
            fail();
        }catch (UserNotExistsException exception){
            String message = exception.getMessage();
            assertEquals("UserNotExistsException: The user entered does not exist", message);
        }
    }
    @Test void testAccountEnable(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        icesiAccountService.enableAccount(icesiAccount);
        assertEquals(true,icesiAccount.isActive());
    }
    @Test
    public void testAccountDisable(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccountService.disableAccount(icesiAccount);
        assertEquals(false,icesiAccount.isActive());
    }
    @Test
    public void testAccountDisableFail(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(10);
        try{
            icesiAccountService.disableAccount(icesiAccount);
            fail();
        }catch (AccountCantBeDisableException exception){
            String message = exception.getMessage();
            assertEquals("AccountCantBeDisableException: account balance is higher than zero",message);
        }
    }
    @Test
    public void testDepositToAccount(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(10);
        icesiAccountService.deposit(icesiAccount,200);
        assertEquals(210,icesiAccount.getBalance());
    }
    @Test
    public void testDepositToAccountDisable(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        try{
            icesiAccountService.deposit(icesiAccount,200);
            fail();
        }catch (AccountDisabledException exception){
            String message = exception.getMessage();
            assertEquals("AccountDisabledException: The account is disabled",message);
        }
    }
    @Test
    public void testWithdrawalToAccount(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(210);
        icesiAccountService.withdrawal(icesiAccount,200);
        assertEquals(10,icesiAccount.getBalance());
    }
    @Test
    public void testWithdrawalToAccountNoMoney(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setBalance(50);
        try{
            icesiAccountService.withdrawal(icesiAccount,60);
            fail();
        }catch (AccountUnableToWithdrawalException exception){
            String message = exception.getMessage();
            assertEquals("AccountUnableToWithdrawalException: You cant get that amount of money out of the account",message);
        }
    }
    @Test
    public void testWithdrawalToAccountDisable(){
        when(icesiAccountRepository.findById(any())).thenReturn(Optional.of(defaultIcesiAccount()));
        IcesiAccount icesiAccount = defaultIcesiAccount();
        icesiAccount.setActive(false);
        try{
            icesiAccountService.withdrawal(icesiAccount,200);
            fail();
        }catch (AccountDisabledException exception){
            String message = exception.getMessage();
            assertEquals("AccountDisabledException: The account is disabled",message);
        }
    }
    @Test
    public void testTranfer(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        IcesiAccount icesiAccount1 = defaultIcesiAccount();
        icesiAccount.setBalance(500);
        icesiAccountService.transfer(icesiAccount1,icesiAccount,200);
        assertEquals(200,icesiAccount1.getBalance());
        assertEquals(300,icesiAccount.getBalance());
    }
    @Test
    public void testTranferFail(){
        IcesiAccount icesiAccount = defaultIcesiAccount();
        IcesiAccount icesiAccount1 = defaultIcesiAccount();
        icesiAccount.setBalance(150);
        try {
            icesiAccountService.transfer(icesiAccount1,icesiAccount,200);
            fail();
        }catch (AccountUnableToTransferException exception){
            String message = exception.getMessage();
            assertEquals("AccountUnableToTransferException: the transfer value is higher than the balance of the account",message);
        }
    }
    private IcesiAccountCreateDTO defaultAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type("Normal")
                .build();
    }
    private IcesiAccountCreateDTO balanceErrorAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(-100)
                .type("Normal")
                .build();
    }
    private IcesiAccountCreateDTO typeErrorAccountCreateDTO(){
        return IcesiAccountCreateDTO.builder()
                .balance(0)
                .type("norm")
                .build();
    }
    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .balance(0)
                .type("Normal")
                .active(true)
                .build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@test.com")
                .phoneNumber("3201112222")
                .password("12345")
                .build();
    }
}
