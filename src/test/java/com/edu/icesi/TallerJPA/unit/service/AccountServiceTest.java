package com.edu.icesi.TallerJPA.unit.service;

import com.edu.icesi.TallerJPA.dto.AccountCreateDTO;
import com.edu.icesi.TallerJPA.mapper.AccountMapper;
import com.edu.icesi.TallerJPA.mapper.AccountMapperImpl;
import com.edu.icesi.TallerJPA.model.IcesiAccount;
import com.edu.icesi.TallerJPA.model.IcesiUser;
import com.edu.icesi.TallerJPA.repository.AccountRepository;
import com.edu.icesi.TallerJPA.service.AccountService;
import com.edu.icesi.TallerJPA.unit.matcher.IcesiAccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private IcesiAccount icesiAccount;

    @BeforeEach
    private void init() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository, accountMapper);
        icesiAccount = createIcesiAccount();
    }

    @Test
    public void testCreateAccount() {
        IcesiAccount icesiAccount = accountService.save(createAccountDTO());
        IcesiAccount icesiAccount1 = IcesiAccount.builder()
                .balance(5000)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount1)));
    }

    @Test
    public void validateAccountNumber(){
        when(accountMapper.fromIcesiAccountDTO(any())).thenReturn(createIcesiAccount());

        AccountCreateDTO accountDTO = createAccountDTO();

        accountService.save(accountDTO);

        verify(accountRepository, times(1)).save(any());

        String accountNumber = accountDTO.getAccountNumber();

        boolean verificationOfAccountNumber = Arrays.stream(accountNumber.split("-")).allMatch(symbol -> Pattern.matches("\\d+",symbol));

        assertTrue(verificationOfAccountNumber);
    }
    @Test
    public void testCreateAccountWithBalanceBelowZero() {

        when(accountMapper.fromIcesiAccountDTO(any())).thenReturn(createIcesiAccount());

        try {
            accountService.save(createAccountDTOWithBalanceBelowZero());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Balance can't be below 0", messageOfException);
        }
    }

    @Test
    public void testDisableExistingAccount() {

        AccountCreateDTO accountWithBalanceZeroDTO = createAccountWithBalanceZeroDTO();

        accountService.save(accountWithBalanceZeroDTO);

        String accountNumber = accountWithBalanceZeroDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));

        verify(accountRepository, times(1)).save(any());

        accountService.setStateAccount(accountWithBalanceZeroDTO, accountNumber);

        assertFalse(accountWithBalanceZeroDTO.isActive());

    }

    @Test
    public void testDisableExistingAccountWithBalanceGreaterThanZero() {

        AccountCreateDTO accountWithBalanceGreaterThanZeroDTO = createAccountDTO();

        accountService.save(accountWithBalanceGreaterThanZeroDTO);

        String accountNumber = accountWithBalanceGreaterThanZeroDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));

        verify(accountRepository, times(1)).save(any());

        try {
            accountService.setStateAccount(accountWithBalanceGreaterThanZeroDTO, accountNumber);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account can't change status", messageOfException);
        }
    }

    @Test
    public void testEnableExistingAccount() {

        AccountCreateDTO accountDisableDTO = createAccountDisableDTO();

        accountService.save(accountDisableDTO);

        String accountNumber = accountDisableDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(icesiAccount));

        verify(accountRepository, times(1)).save(any());

        accountService.setStateAccount(accountDisableDTO, accountNumber);

        assertTrue(accountDisableDTO.isActive());
    }

    @Test
    public void testSetStatusNotExistingAccount() {

        AccountCreateDTO accountCreateDTO = createAccountDTO();
        accountCreateDTO.setAccountNumber("13-154789-10");

        when(accountRepository.findByAccountNumber(accountCreateDTO.getAccountNumber())).thenReturn(Optional.empty());
        verify(accountRepository, times(0)).save(any());

        try {
            accountService.setStateAccount(accountCreateDTO, anyString());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account not found", messageOfException);
        }

    }

    @Test
    public void testWithdrawWithEnoughMoneyFromNormalAccount() {

        AccountCreateDTO accountNormalDTO = createAccountNormalDTO();

        accountService.save(accountNormalDTO);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfNormalAccount = accountNormalDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfNormalAccount)).thenReturn(Optional.of(icesiAccount));

        accountService.withdrawals(accountNormalDTO, accountNumberOfNormalAccount, 1000);

        assertEquals(4000, accountNormalDTO.getBalance());
    }

    @Test
    public void testWithdrawWithEnoughMoneyFromDepositOnlyAccount() {

        AccountCreateDTO accountDepositOnlyDTO = createAccountDTO();

        accountService.save(accountDepositOnlyDTO);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfDepositOnlyAccount = accountDepositOnlyDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfDepositOnlyAccount)).thenReturn(Optional.of(icesiAccount));

        accountService.withdrawals(accountDepositOnlyDTO, accountNumberOfDepositOnlyAccount, 1000);

        assertEquals(4000, accountDepositOnlyDTO.getBalance());
    }

    @Test
    public void testWithdrawWithInsufficientMoneyFromNormalAccount() {

        AccountCreateDTO accountNormalDTO = createAccountNormalDTO();

        accountService.save(accountNormalDTO);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfNormalAccount = accountNormalDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfNormalAccount)).thenReturn(Optional.of(icesiAccount));

        try {
            accountService.withdrawals(accountNormalDTO, accountNumberOfNormalAccount, 6000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Insufficient money", messageOfException);
        }
    }

    @Test
    public void testWithdrawWithInsufficientMoneyFromDepositOnlyAccount() {

        AccountCreateDTO accountDepositOnlyDTO = createAccountDTO();

        accountService.save(accountDepositOnlyDTO);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfDepositOnlyAccount = accountDepositOnlyDTO.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfDepositOnlyAccount)).thenReturn(Optional.of(icesiAccount));

        try {
            accountService.withdrawals(accountDepositOnlyDTO, accountNumberOfDepositOnlyAccount, 6000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Insufficient money", messageOfException);
        }
    }

    @Test
    public void testDepositMoneyNormalAccount() {

        AccountCreateDTO accountToDeposit = createAccountNormalDTO();

        accountService.save(accountToDeposit);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfNormalAccount = accountToDeposit.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfNormalAccount)).thenReturn(Optional.of(createIcesiAccount()));

        accountService.depositMoney(accountToDeposit, accountNumberOfNormalAccount, 15000);

        assertEquals(20000, accountToDeposit.getBalance());
    }

    @Test
    public void testDepositInvalidMoneyNormalAccount() {

        AccountCreateDTO accountToDeposit = createAccountNormalDTO();

        accountService.save(accountToDeposit);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfAccountToDeposit = accountToDeposit.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfAccountToDeposit)).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.depositMoney(accountToDeposit, accountNumberOfAccountToDeposit, -2000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Invalid value", messageOfException);
        }
    }

    @Test
    public void testDepositMoneyDepositOnlyAccount() {

        AccountCreateDTO accountToDeposit = createAccountWithBalanceZeroDTO();

        accountService.save(accountToDeposit);

        verify(accountRepository, times(1)).save(any());

        accountService.save(accountToDeposit);

        String accountNumberOfAccountToDeposit = accountToDeposit.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfAccountToDeposit)).thenReturn(Optional.of(createIcesiAccount()));

        accountService.depositMoney(accountToDeposit, accountNumberOfAccountToDeposit, 8000);

        assertEquals(8000, accountToDeposit.getBalance());
    }

    @Test
    public void testDepositInvalidMoneyDepositOnlyAccount() {

        AccountCreateDTO accountToDeposit = createAccountDTO();

        accountService.save(accountToDeposit);

        verify(accountRepository, times(1)).save(any());

        String accountNumberOfAccountToDeposit = accountToDeposit.getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNumberOfAccountToDeposit)).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.depositMoney(accountToDeposit, accountNumberOfAccountToDeposit, 0);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Invalid value", messageOfException);
        }
    }

    @Test
    public void testTransferMoneyToNormalAccount() {

        AccountCreateDTO sourceAccount = createAccountNormalDTO();
        AccountCreateDTO destinationAccount = createAccountNormalDTO1();

        accountService.save(sourceAccount);
        accountService.save(destinationAccount);

        verify(accountRepository, times(2)).save(any());

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        accountService.transferMoney(sourceAccount, destinationAccount, 1000);
        assertEquals(4000, sourceAccount.getBalance());
        assertEquals(8000, destinationAccount.getBalance());
    }

    @Test
    public void testTransferMoneyToNormalAccountFromInsufficientMoneyAccount() {

        AccountCreateDTO sourceAccount = createAccountNormalDTO();
        AccountCreateDTO destinationAccount = createAccountNormalDTO1();

        accountService.save(sourceAccount);
        accountService.save(destinationAccount);

        verify(accountRepository, times(2)).save(any());

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.transferMoney(sourceAccount, destinationAccount, 8000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Insufficient money to make a transfer", messageOfException);

        }
    }

    @Test
    public void testTransferMoneyToNormalAccountWithInvalidValue() {

        AccountCreateDTO sourceAccount = createAccountNormalDTO();
        AccountCreateDTO destinationAccount = createAccountNormalDTO1();

        accountService.save(sourceAccount);
        accountService.save(destinationAccount);

        verify(accountRepository, times(2)).save(any());

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.transferMoney(sourceAccount, destinationAccount, 0);
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Invalid value", messageOfException);

        }
    }


    @Test
    public void testTransferMoneyToDepositOnlyAccount() {

        AccountCreateDTO sourceDepositOnlyAccount = createAccountDTO();
        AccountCreateDTO destinationAccount = createAccountNormalDTO1();

        accountService.save(sourceDepositOnlyAccount);
        accountService.save(destinationAccount);

        verify(accountRepository, times(2)).save(any());

        when(accountRepository.findByAccountNumber(sourceDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(accountRepository.findByAccountNumber(destinationAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.transferMoney(sourceDepositOnlyAccount, destinationAccount, 1000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("It is not possible to make the transfer. At least one account is deposit only", messageOfException);

        }
    }

    @Test
    public void testTransferMoneyFromDepositOnlyAccount() {

        AccountCreateDTO sourceAccount = createAccountNormalDTO1();
        AccountCreateDTO destinationDepositOnlyAccount = createAccountDTO();

        accountService.save(sourceAccount);
        accountService.save(destinationDepositOnlyAccount);

        verify(accountRepository, times(2)).save(any());

        when(accountRepository.findByAccountNumber(sourceAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        when(accountRepository.findByAccountNumber(destinationDepositOnlyAccount.getAccountNumber())).thenReturn(Optional.of(createIcesiAccount()));

        try {
            accountService.transferMoney(sourceAccount, destinationDepositOnlyAccount, 1000);
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("It is not possible to make the transfer. At least one account is deposit only", messageOfException);
        }
    }

    public AccountCreateDTO createAccountDTO() {
        return AccountCreateDTO.builder()
                .balance(5000)
                .type("Deposit only")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public AccountCreateDTO createAccountWithBalanceZeroDTO() {
        return AccountCreateDTO.builder()
                .balance(0)
                .type("Deposit only")
                .active(true)
                .build();
    }

    public AccountCreateDTO createAccountNormalDTO() {
        return AccountCreateDTO.builder()
                .balance(5000)
                .type("Normal")
                .active(true)
                .build();
    }

    public AccountCreateDTO createAccountNormalDTO1() {
        return AccountCreateDTO.builder()
                .balance(7000)
                .type("Normal")
                .active(true)
                .build();
    }

    public AccountCreateDTO createAccountDisableDTO() {
        return AccountCreateDTO.builder()
                .balance(5000)
                .type("Deposit Only")
                .active(false)
                .build();
    }

    public AccountCreateDTO createAccountDTOWithBalanceBelowZero() {
        return AccountCreateDTO.builder()
                .balance(-1)
                .type("Deposit only")
                .active(true)
                .build();
    }

    public IcesiAccount createIcesiAccount() {
        return IcesiAccount.builder()
                .balance(5000)
                .type("Normal")
                .active(true)
                .icesiUser(createIcesiUser())
                .build();
    }

    public IcesiUser createIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("example@exampleEmail.com")
                .phoneNumber("1234567")
                .password("1234")
                .build();
    }
}
