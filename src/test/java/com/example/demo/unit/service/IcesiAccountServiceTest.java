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
import com.example.demo.mapper.IcesiAccountMapper;
import com.example.demo.mapper.IcesiAccountMapperImpl;
import com.example.demo.model.IcesiAccount;
import com.example.demo.model.IcesiRole;
import com.example.demo.model.IcesiUser;
import com.example.demo.repository.IcesiAccountRepository;
import com.example.demo.repository.IcesiUserRepository;
import com.example.demo.service.IcesiAccountService;

public class IcesiAccountServiceTest {
    private IcesiAccountService icesiAccountService;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiAccountMapper icesiAccountMapper;

    private IcesiUserRepository icesiUserRepository;

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
            .type("deposit")
            .active(true)
            .icesiUser(defaultIcesiUser())
            .build();
    }
    
    private IcesiAccount defaultIcesiAccount() {
        return IcesiAccount.builder()
            .accountNumber("123-123456-12")
            .balance(0)
            .type("deposit")
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

    @Test
    public void testCreateIcesiAccount() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiAccountService.create(defaultIcesiAccountCreateDTO());
        IcesiAccount icesiAccount1 = IcesiAccount.builder()
            .accountNumber("123-123456-12")
            .balance(0)
            .type("deposit")
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
        assertEquals("This accout cannot be disabled, its balances is not 0", message);
    }

    @Test 
    public void testWitdrawalFromDisabledAccount() {
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        account.setActive(false);
        account.setBalance(300L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.withdrawalMoney(100L, account));
        String message = exception.getMessage();
        assertEquals("Account is disabled, it is not possible to withdraw form it", message);
    }

    @Test 
    public void testWithdrawalWithSufficientFunds() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        account.setBalance(1000L);
        icesiAccountService.create(account);
        icesiAccountService.withdrawalMoney(300L, account);
        assertEquals(account.getBalance(), 700L);
    }

    @Test 
    public void testWithdrawalWithInsufficientFunds() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        account.setBalance(1000L);
        icesiAccountService.create(account);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.withdrawalMoney(3000L, account));
        String message = exception.getMessage();
        assertEquals("This account does not have enough funds", message);
    }

    @Test
    public void testDepositMoney() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiAccountCreateDTO account = defaultIcesiAccountCreateDTO();
        icesiAccountService.create(account);
        icesiAccountService.depositMoney(3405L, account);
        assertEquals(account.getBalance(), 3405L);
    }

    @Test
    public void testTransferFromDepositAccount() {
        IcesiAccountCreateDTO originAccount = defaultIcesiAccountCreateDTO();
        IcesiAccountCreateDTO destinationAccount = defaultIcesiAccountCreateDTO();
        originAccount.setBalance(999L);
        destinationAccount.setType("normal");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(300L, originAccount, destinationAccount));
        String message = exception.getMessage();
        assertEquals("The origin account is not allowed to be transfer money", message);
    }

    @Test
    public void testTransferToDepositAccount() {
        IcesiAccountCreateDTO originAccount = defaultIcesiAccountCreateDTO();
        IcesiAccountCreateDTO destinationAccount = defaultIcesiAccountCreateDTO();
        originAccount.setBalance(999L);
        originAccount.setType("normal");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(300L, originAccount, destinationAccount));
        String message = exception.getMessage();
        assertEquals("The destination account is not allowed to be transferred money", message);
    }

    @Test
    public void testTransferFromAccountWithInsufficientFunds() {
        IcesiAccountCreateDTO originAccount = defaultIcesiAccountCreateDTO();
        IcesiAccountCreateDTO destinationAccount = defaultIcesiAccountCreateDTO();
        originAccount.setType("normal");
        destinationAccount.setType("normal");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(300L, originAccount, destinationAccount));
        String message = exception.getMessage();
        assertEquals("The origin account does not have enough funds", message);
    }

    @Test
    public void testSuccessfulTransfer() {
        IcesiAccountCreateDTO originAccount = defaultIcesiAccountCreateDTO();
        IcesiAccountCreateDTO destinationAccount = defaultIcesiAccountCreateDTO();
        originAccount.setType("normal");
        originAccount.setBalance(444L);
        destinationAccount.setType("normal");
        icesiAccountService.transferMoneyToAnotherAccount(300L, originAccount, destinationAccount);
        assertEquals(destinationAccount.getBalance(), 300L);
        assertEquals(originAccount.getBalance(), 144L);
    }

    @Test
    public void testTransferFromDisabledAccount() {
        IcesiAccountCreateDTO originAccount = defaultIcesiAccountCreateDTO();
        IcesiAccountCreateDTO destinationAccount = defaultIcesiAccountCreateDTO();
        originAccount.setType("normal");
        originAccount.setActive(false);
        destinationAccount.setType("normal");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(300L, originAccount, destinationAccount));
        String message = exception.getMessage();
        assertEquals("The origin account is disabled", message);
    }

    @Test
    public void testTransferToDisabledAccount() {
        IcesiAccountCreateDTO originAccount = defaultIcesiAccountCreateDTO();
        IcesiAccountCreateDTO destinationAccount = defaultIcesiAccountCreateDTO();
        originAccount.setType("normal");
        destinationAccount.setType("normal");
        destinationAccount.setActive(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> icesiAccountService.transferMoneyToAnotherAccount(300L, originAccount, destinationAccount));
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
