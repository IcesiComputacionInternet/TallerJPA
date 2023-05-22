package com.example.demo.unit.service;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;

import com.example.demo.DTO.IcesiAccountCreateDTO;
import com.example.demo.DTO.TransactionCreateDTO;
import com.example.demo.error.exception.IcesiException;
import com.example.demo.mapper.IcesiAccountMapper;
import com.example.demo.mapper.IcesiAccountMapperImpl;
import com.example.demo.model.IcesiAccount;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.model.enums.TypeIcesiAccount;
import com.example.demo.repository.IcesiAccountRepository;
import com.example.demo.repository.IcesiUserRepository;
import com.example.demo.service.IcesiAccountService;

public class IcesiAccountServiceTest {
    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper icesiAccountMapper;

    private IcesiUserRepository icesiUserRepository;

    private TypeIcesiAccount typeIcesiAccount;

    @BeforeEach
    private void init() {
        icesiAccountMapper = spy(IcesiAccountMapperImpl.class);
        icesiAccountRepository = mock(IcesiAccountRepository.class);
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiAccountService = new IcesiAccountService(icesiAccountRepository, icesiAccountMapper, icesiUserRepository);
    }

    private IcesiAccountCreateDTO defaultIcesiAccountCreateDTO() {
        return IcesiAccountCreateDTO.builder()
            .accountNumber("123-123456-12")
            .balance(0)
            .type(typeIcesiAccount.deposit)
            .active(true)
            .icesiUser(defaultIcesiUser())
            .build();
    }
    
    private IcesiAccount defaultIcesiAccount() {
        return IcesiAccount.builder()
            .accountNumber("123-123456-12")
            .balance(0)
            .type(typeIcesiAccount.deposit)
            .active(true)
            .icesiUser(defaultIcesiUser())
            .build();
    }

    private IcesiUser defaultIcesiUser() {
        return IcesiUser.builder()
            .firstName("John")
            .lastName("Doe")
            .email("testEmail@example.com")
            .phoneNumber("999999")
            .password("1234")
            .icesiRole(defaultIcesiRole())
            .build();
    }

    private IcesiRole defaultIcesiRole() {
        return IcesiRole.builder()
            .description("This role is for students")
            .name("Student")
            .build();
    }

    private TransactionCreateDTO defaultTransactionCreateDTO() {
        return TransactionCreateDTO.builder()
            .senderAccountNumber("123-123456-12")
            .receiverAccountNumber("698-481203-30")
            .amount(300L)
            .build();
    }

    @Test
    public void testCreateIcesiAccount() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.create(defaultIcesiAccountCreateDTO());
        IcesiAccount icesiAccount1 = IcesiAccount.builder()
            .accountNumber("123-123456-12")
            .balance(0)
            .type(typeIcesiAccount.deposit)
            .active(true)
            .icesiUser(defaultIcesiUser())
            .build();
        verify(icesiAccountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount1)));
    }

    @Test
    public void testEnableAccount() {
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        icesiAccountService.enableAccount(account);
        assertTrue(account.isActive());
    }

    @Test
    public void testDisableAccountWithZeroBlanace() {
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        icesiAccountService.disableAccount(account);
        assertFalse(account.isActive());
    }

    @Test
    public void testDisableAccountWithNoZeroBalance() {
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        account.setBalance(100L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.disableAccount(account));
        String message = exception.getMessage();
        assertEquals("This account cannot be disabled, its balance is not 0", message);
    }

    @Test 
    public void testWitdrawalFromDisabledAccount() {
        
        IcesiAccount account = defaultIcesiAccount();
        account.setActive(false);
        account.setBalance(300L);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(account));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.withdrawalMoney(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("Account is disabled, it is not possible to withdraw from it", message);
    }

    @Test 
    public void testWithdrawalWithSufficientFunds() {  

        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000L);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(account));

        icesiAccountService.withdrawalMoney(defaultTransactionCreateDTO());
        assertEquals(account.getBalance(), 700L);
    }

    @Test 
    public void testWithdrawalWithInsufficientFunds() {
        
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(100L);
        
        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(account));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.withdrawalMoney(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("This account does not have enough funds", message);
    }

    @Test
    public void testDepositMoney() {
        IcesiAccount account = defaultIcesiAccount();

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(account));

        icesiAccountService.depositMoney(defaultTransactionCreateDTO());
        assertEquals(account.getBalance(), 300L);
    }

    @Test
    public void testTransferFromDepositAccount() {
        IcesiAccount originAccount = defaultIcesiAccount();
        IcesiAccount destinationAccount = defaultIcesiAccount();
        originAccount.setBalance(999L);
        destinationAccount.setType(typeIcesiAccount.normal);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(originAccount));
        when(icesiAccountRepository.findByAccountNumber("698-481203-30")).thenReturn(Optional.of(destinationAccount));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("The origin account is not allowed to transfer money", message);
    }

    @Test
    public void testTransferToDepositAccount() {

        IcesiAccount originAccount = defaultIcesiAccount();
        IcesiAccount destinationAccount = defaultIcesiAccount();
        originAccount.setBalance(999L);
        originAccount.setType(typeIcesiAccount.normal);
        destinationAccount.setType(typeIcesiAccount.deposit);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(originAccount));
        when(icesiAccountRepository.findByAccountNumber("698-481203-30")).thenReturn(Optional.of(destinationAccount));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("The destination account is not allowed to receive transferred money", message);
    }

    @Test
    public void testTransferFromAccountWithInsufficientFunds() {

        IcesiAccount originAccount = defaultIcesiAccount();
        IcesiAccount destinationAccount = defaultIcesiAccount();
        originAccount.setType(typeIcesiAccount.normal);
        destinationAccount.setType(typeIcesiAccount.normal);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(originAccount));
        when(icesiAccountRepository.findByAccountNumber("698-481203-30")).thenReturn(Optional.of(destinationAccount));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("The origin account does not have enough funds", message);
    }

    @Test
    public void testSuccessfulTransfer() {
        IcesiAccount originAccount = defaultIcesiAccount();
        IcesiAccount destinationAccount = defaultIcesiAccount();
        originAccount.setType(typeIcesiAccount.normal);
        originAccount.setBalance(444L);
        destinationAccount.setType(typeIcesiAccount.normal);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(originAccount));
        when(icesiAccountRepository.findByAccountNumber("698-481203-30")).thenReturn(Optional.of(destinationAccount));

        icesiAccountService.transferMoneyToAnotherAccount(defaultTransactionCreateDTO());
        assertEquals(destinationAccount.getBalance(), 300L);
        assertEquals(originAccount.getBalance(), 144L);
    }

    @Test
    public void testTransferFromDisabledAccount() {

        IcesiAccount originAccount = defaultIcesiAccount();
        IcesiAccount destinationAccount = defaultIcesiAccount();
        originAccount.setType(typeIcesiAccount.normal);
        originAccount.setActive(false);
        destinationAccount.setType(typeIcesiAccount.normal);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(originAccount));
        when(icesiAccountRepository.findByAccountNumber("698-481203-30")).thenReturn(Optional.of(destinationAccount));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("The origin account is disabled", message);
    }

    @Test
    public void testTransferToDisabledAccount() {

        IcesiAccount originAccount = defaultIcesiAccount();
        IcesiAccount destinationAccount = defaultIcesiAccount();
        originAccount.setType(typeIcesiAccount.normal);
        destinationAccount.setType(typeIcesiAccount.normal);
        destinationAccount.setActive(false);

        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(originAccount));
        when(icesiAccountRepository.findByAccountNumber("698-481203-30")).thenReturn(Optional.of(destinationAccount));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(defaultTransactionCreateDTO()));
        String message = exception.getMessage();
        assertEquals("The destination account is disabled", message);
    }


    @Test
    public void testCreateAccountWithNegativeBalance() {
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        account.setBalance(-100L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.create(account));
        String message = exception.getMessage();
        assertEquals("The account balance cannot be negative", message);
    }

    @Test
    public void testCreateAccountWithExistingAccountNumber() {
        when(icesiAccountRepository.findByAccountNumber("123-123456-12")).thenReturn(Optional.of(defaultIcesiAccount()));
        try {
            icesiAccountService.create(defaultIcesiAccountCreateDTO());
            fail();
        } catch (Exception exception) {
            String message = exception.getMessage();
            assertEquals("This account number is already in use", message);
        }
    }

}
