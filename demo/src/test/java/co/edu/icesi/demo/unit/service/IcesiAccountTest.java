package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.IcesiAccountDto;
import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.dto.TransactionOperationDto;
import co.edu.icesi.demo.enums.TypeAccount;
import co.edu.icesi.demo.mapper.IcesiAccountMapper;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiAccountRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import co.edu.icesi.demo.service.IcesiAccountService;
import co.edu.icesi.demo.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class IcesiAccountTest {

    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper icesiAccountMapper;

    private IcesiUserRepository icesiUserRepository;

    @BeforeEach
    public void setup(){
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiAccountMapper = spy(IcesiAccountMapper.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper, icesiUserRepository);
    }



    @Test
    public void testSaveAccount(){
        /*when(icesiUserRepository.findById(any())).thenReturn(Optional.ofNullable(defaultCreateUser()));
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.ofNullable(createDefaultIcesiUser()));
        icesiAccountService.save(createDefaultDTOPositiveBalanceAccount());
        IcesiAccount expectedAccount= createDefaultPositiveBalanceAccount();

        verify(icesiAccountRepository,times(1)).save(argThat(new IcesiAccountMatcher(expectedAccount)));*/
    }

    @Test
    public void testSaveAccountUserDoesNotExist(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        try{
            icesiAccountService.save(createDefaultDTOPositiveBalanceAccount());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User not found",message);
        }
    }

    @Test
    public void testDisableAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefault0BalanceAccount()));
        icesiAccountService.disableAccount(createDefaultDTO0BalanceAccount().getAccountNumber());
        IcesiAccount expectedAccount= icesiAccountRepository.findByNumber(createDefaultDTO0BalanceAccount().getAccountNumber()).get();
        assertFalse(expectedAccount.isActive());
    }

    @Test
    public void testDisableAccountNot0Balance(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            icesiAccountService.disableAccount(createDefaultDTOPositiveBalanceAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account must have 0 balance to be disabled",message);
        }
    }
    @Test
    public void enableNotExistentAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            icesiAccountService.enableAccount(createDefaultDTOPositiveBalanceAccount().getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account " + createTransactionOperationDTO().getAccountFrom() + " does not exist",message);
        }
    }

    @Test
    public void withdraw(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        icesiAccountService.withdraw(createTransactionOperationDTO());
        IcesiAccount expectedAccount= icesiAccountRepository.findByNumber(createDefaultDTOPositiveBalanceAccount().getAccountNumber()).get();
        assertEquals(expectedAccount.getBalance(), 999000);
    }


    @Test
    public void withdrawNotExistentAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            icesiAccountService.withdraw(createTransactionOperationDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account " + createTransactionOperationDTO().getAccountFrom() + " does not exist",message);
        }
    }

    @Test
    public void withdrawNotActiveAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultDisabledAccount()));
        try{
            icesiAccountService.withdraw(createTransactionOperationDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account is disabled",message);
        }
    }

    @Test
    public void withdrawMoreThanBalance(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            icesiAccountService.withdraw(TransactionOperationDto.builder()
                    .accountFrom("")
                    .amount(1000001L)
                    .build());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account must have balance greater than the amount to transfer",message);
        }
    }

    @Test
    public void deposit(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        icesiAccountService.deposit(createTransactionOperationDTO());
        IcesiAccount expectedAccount= icesiAccountRepository.findByNumber(createDefaultDTOPositiveBalanceAccount().getAccountNumber()).get();
        assertEquals(expectedAccount.getBalance(), 1001000);
    }

    @Test
    public void depositNotExistentAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            icesiAccountService.deposit(createTransactionOperationDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account " + createTransactionOperationDTO().getAccountFrom() + " does not exist",message);
        }
    }

    @Test
    public void depositNotActiveAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultDisabledAccount()));
        try{
            icesiAccountService.deposit(createTransactionOperationDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account is disabled",message);
        }
    }

    @Test
    public void depositNegativeAmount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            icesiAccountService.deposit(TransactionOperationDto.builder()
                    .accountFrom("")
                    .amount(-1000L)
                    .build());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Amount must be greater than 0",message);
        }
    }

    @Test
    public void transfer() {

        when(icesiAccountRepository.findByNumber("123-456789-10")).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        when(icesiAccountRepository.findByNumber("123-456789-12")).thenReturn(Optional.ofNullable(createDefault0BalanceAccount()));

        icesiAccountService.transfer(createTransactionOperationDTO());
        IcesiAccount sourceAccount = icesiAccountRepository.findByNumber(createDefaultPositiveBalanceAccount().getAccountNumber()).get();
        IcesiAccount targetAccount = icesiAccountRepository.findByNumber(createDefault0BalanceAccount().getAccountNumber()).get();

        verify(icesiAccountRepository, times(2)).save(any());

        assertEquals(999000L,sourceAccount.getBalance());

        assertEquals(1000L,targetAccount.getBalance());

    }

    @Test
    public void transferNotExistentSourceAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.empty());
        try{
            icesiAccountService.transfer(createTransactionOperationDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account " + createTransactionOperationDTO().getAccountFrom() + " does not exist",message);
        }
    }

    @Test void transferDepositAccount(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDepositAccount()));
        try{
            icesiAccountService.transfer(createTransactionOperationDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account: " + createDepositAccount().getAccountNumber() + " is deposit only",message);
        }
    }

    @Test
    public void testTransferNotEnoughBalance(){
        when(icesiAccountRepository.findByNumber(any())).thenReturn(Optional.ofNullable(createDefaultPositiveBalanceAccount()));
        try{
            icesiAccountService.transfer(TransactionOperationDto.builder()
                    .accountFrom("")
                    .amount(1000001L)
                    .build());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account must have balance greater than the amount to transfer",message);
        }
    }

    private IcesiUser createDefaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Juan")
                .lastName("Blanco")
                .email("cryinginside@gmail.com")
                .password("1234568")
                .phoneNumber("3173245376")
                .role(defaultCreateRole())
                .build();
    }

    private IcesiUserDto createDefaultIcesiUserDto(){
        return IcesiUserDto.builder()
                .userId(UUID.randomUUID())
                .firstname("Juan")
                .lastName("Blanco")
                .email("cryinginside@gmail.com")
                .password("1234568")
                .phoneNumber("3173245376")
                .role(defaultCreateRoleDTO())
                .build();
    }
    private IcesiAccount createDefaultPositiveBalanceAccount(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-10")
                .type("Default")
                .user(defaultCreateUser())
                .balance(1000000L)
                .active(true)
                .build();
    }

    private TransactionOperationDto createTransactionOperationDTO(){
        return TransactionOperationDto.builder()
                .accountFrom("123-456789-10")
                .accountTo("123-456789-12")
                .amount(1000L)
                .build();
    }

    private IcesiAccountDto createDefaultDTOPositiveBalanceAccount(){
        return IcesiAccountDto.builder()
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
                .typeAccount(TypeAccount.DEPOSIT_ONLY)
                .user(defaultCreateUser())
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
                .balance(0L)
                .active(true)
                .build();
    }

    private IcesiAccountDto createDefaultDTO0BalanceAccount(){
        return IcesiAccountDto.builder()
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

    private IcesiUserDto defaultCreateUserDTO(){
        return IcesiUserDto.builder()
                .userId(UUID.randomUUID())
                .firstname("John")
                .lastName("Doe")
                .email("example@example.com")
                .password("123456")
                .phoneNumber("1234567890")
                .role(defaultCreateRoleDTO())
                .build();
    }

    private IcesiRoleDto defaultCreateRoleDTO(){
        return IcesiRoleDto.builder()
                .roleId(UUID.randomUUID())
                .name("ROLE_USER")
                .build();
    }

    private IcesiRole defaultCreateRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .roleName("ROLE_USER")
                .build();
    }
}
