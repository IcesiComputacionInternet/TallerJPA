package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.mapper.AccountMapperImpl;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.model.Role;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.service.AccountService;
import co.com.icesi.tallerjpa.unit.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);

        accountService = new AccountService(accountRepository, accountMapper, userRepository);
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
        verify(accountMapper, times(1)).fromAccountDTO(any());
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
    }

    @Test
    public void testDeposit() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        accountService.deposit(5L,"12345");

        assertEquals(105L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
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
    }

    @Test
    public void testTransfer() {
        Account account = defaultAccount();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        Account account2 = Account.builder()
                .balance(100L)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(java.util.Optional.of(account2));

        accountService.transfer(5L,"12345", "54321");

        assertEquals(95L, account.getBalance());
        assertEquals(105L, account2.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(2)).updateBalance(any(), any());
    }

    @Test
    public void testTransferAccount2IsNotReceiver(){
        Account account = defaultAccount();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        Account account2 = Account.builder()
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(java.util.Optional.of(account2));

        try {
            accountService.transfer(5L,"12345", "54321");
        } catch (RuntimeException e) {
            assertEquals("The account type does not allow transfers", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
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

    private RequestAccountDTO defaultAccountDTO() {
        return RequestAccountDTO.builder()
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .user("prueba@gmail.com")
                .build();
    }

    private Account defaultAccount() {
        return Account.builder()
                .accountNumber("12345")
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
