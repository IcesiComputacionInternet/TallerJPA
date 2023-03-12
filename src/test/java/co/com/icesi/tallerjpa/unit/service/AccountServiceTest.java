package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.AccountDTO;
import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.mapper.AccountMapperImpl;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.service.AccountService;
import co.com.icesi.tallerjpa.strategy.accounts.AccountDepositOnly;
import co.com.icesi.tallerjpa.strategy.accounts.interfaces.TypeAccountStrategy;
import co.com.icesi.tallerjpa.unit.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;
    private List<TypeAccountStrategy> typeAccountStrategies;

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        typeAccountStrategies = List.of(spy(AccountDepositOnly.class));

        accountService = new AccountService(accountRepository, accountMapper, userRepository, typeAccountStrategies);
    }

    @Test
    public void testCreateAccount() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountDTO());

        verify(userRepository, times(1)).findByEmail(any());
        verify(accountMapper, times(1)).fromAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(defaultAccount())));
    }
    @Test
    public void testCreateAccountWithUserNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.empty());

        try {
            accountService.save(defaultAccountDTO());
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userRepository, times(1)).findByEmail(any());
        verify(accountMapper, times(0)).fromAccountDTO(any());
        verify(accountRepository, times(0)).save(argThat(new AccountMatcher(defaultAccount())));
    }

    @Test
    public void testGetAccountByAccountNumber() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(defaultAccount()));

        Account account = accountService.getAccountByAccountNumber("12345");

        assertEquals(defaultAccount(), account);
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }
    @Test
    public void testGetAccountByAccountNumberNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.empty());

        try {
            accountService.getAccountByAccountNumber("12345");
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testWithdraw() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        accountService.withdraw(5L,"12345");

        assertEquals(95L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(1)).withdraw(any(), any());
        verify(accountRepository, times(1)).updateBalance(any(), any());
    }

    @Test
    public void testWithdrawAccountNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.empty());

        try {
            accountService.withdraw(5L,"12345");
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(0)).withdraw(any(), any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testWithdrawAmountNegative() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        try {
            accountService.withdraw(-5L,"12345");
        } catch (RuntimeException e) {
            assertEquals("The amount must be greater than 0", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(1)).withdraw(any(), any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testWithdrawAmountGreaterThanBalance() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        try {
            accountService.withdraw(105L,"12345");
        } catch (RuntimeException e) {
            assertEquals("Insufficient funds", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(1)).withdraw(any(), any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testDeposit() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        accountService.deposit(5L,"12345");

        assertEquals(105L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(1)).deposit(any(), any());
        verify(accountRepository, times(1)).updateBalance(any(), any());
    }

    @Test
    public void testDepositAccountNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.empty());

        try {
            accountService.deposit(5L,"12345");
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(0)).deposit(any(), any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testDepositAmountNegative() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        try {
            accountService.deposit(-5L,"12345");
        } catch (RuntimeException e) {
            assertEquals("The amount must be greater than 0", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(typeAccountStrategies.get(0), times(1)).deposit(any(), any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testTransferWithNoAccountNotAccepted() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        try {
            accountService.transfer(5L,"12345", "54321");
        } catch (UnsupportedOperationException e) {
            assertEquals("This account type does not transfer", e.getMessage());
        }
        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testEnableOrDisableAccount() {
        Account account = defaultAccount();
        accountService.enableOrDisableAccount("12345");

        assertTrue(account.isActive());
        verify(accountRepository, times(1)).enableOrDisableAccount(any());
        verify(accountRepository, times(1)).isActive(any());
    }

    private AccountDTO defaultAccountDTO() {
        return AccountDTO.builder()
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .user(defaultIcesiUser())
                .build();
    }

    private Account defaultAccount() {
        return Account.builder()
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role(defaultRole())
                .build();
    }

    private Role defaultRole(){
        return Role.builder()
                .roleId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .name("Ingeniero")
                .description("Ingeniero de sistemas")
                .build();
    }
}
