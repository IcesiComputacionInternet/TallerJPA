package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.RequestAccountDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.error.exception.CustomException;
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

import java.util.Optional;
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
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountDTO());

        verify(userRepository, times(1)).findByEmail(any());
        verify(accountMapper, times(1)).fromAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(defaultAccount())));
    }
    @Test
    public void testCreateAccountWithUserNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

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
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(defaultAccount()));

        Account account = accountService.getAccountByAccountNumber("12345");

        assertEquals(defaultAccount().getAccountNumber(), account.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }
    @Test
    public void testGetAccountByAccountNumberNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());

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
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        accountService.withdraw(TransactionDTO.builder()
                .accountNumberOrigin("12345")
                .amount(5L)
                .build()
        );


        assertEquals(95L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(accountRepository, times(1)).updateBalance(any(), any());
    }

    @Test
    public void testWithdrawAccountNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());

        try {
            accountService.withdraw(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());

    }

    @Test
    public void testWithdrawAmountGreaterThanBalance() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        try {
            accountService.withdraw(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .amount(105L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("Insufficient funds", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testWithdrawAccountIsNotActive() {
        Account account = defaultAccount();
        account.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        try {
            accountService.withdraw(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("The account 12345 is not active", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testDeposit() {
        Account account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        accountService.deposit(TransactionDTO.builder()
                .accountNumberOrigin("12345")
                .amount(5L)
                .build()
        );

        assertEquals(105L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(accountRepository, times(1)).updateBalance(any(), any());
    }

    @Test
    public void testDepositAccountNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());

        try {
            accountService.deposit(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testDepositAccountIsNotActive() {
        Account account = defaultAccount();
        account.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        try {
            accountService.deposit(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("The account 12345 is not active", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testTransfer() {
        Account account = defaultAccount();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        Account account2 = Account.builder()
                .balance(100L)
                .active(true)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(Optional.of(account2));

        accountService.transfer(TransactionDTO.builder()
                .accountNumberOrigin("12345")
                .accountNumberDestination("54321")
                .amount(5L)
                .build()
        );

        assertEquals(95L, account.getBalance());
        assertEquals(105L, account2.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(2)).updateBalance(any(), any());
    }

    @Test
    public void testTransferAccountIsNotActive() {
        Account account = defaultAccount();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        Account account2 = Account.builder()
                .balance(100L)
                .active(false)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(Optional.of(account2));

        try {
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .accountNumberDestination("54321")
                    .amount(5L)
                    .build()
            );
        } catch (RuntimeException e) {
            assertEquals("The account 54321 is not active", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testTransferAccount2IsNotReceiver(){
        Account account = defaultAccount();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        Account account2 = Account.builder()
                .balance(100L)
                .active(true)
                .type(TypeAccount.DEPOSIT_ONLY)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(Optional.of(account2));

        try {
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .accountNumberDestination("54321")
                    .amount(5L)
                    .build()
            );
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
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        try {
            accountService.transfer(TransactionDTO.builder()
                    .accountNumberOrigin("12345")
                    .accountNumberDestination("54321")
                    .amount(5L)
                    .build()
            );
        } catch (UnsupportedOperationException e) {
            assertEquals("This account type does not transfer", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testDisableAccountCantBeDisabledButUserIsOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(true);
        when(accountRepository.isActive(any())).thenReturn(true);
        TransactionDTO msg = accountService.disableAccount(any(), any());

        assertEquals("The account can't be disabled", msg.getMessage());
        verify(accountRepository, times(1)).disableAccount(any());
        verify(accountRepository, times(1)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testDisableAccountCantBeDisabledButUserIsNotOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(false);
        when(accountRepository.isActive(any())).thenReturn(true);

        try {
            accountService.enableAccount(any(), any());
        } catch (CustomException e) {
            assertEquals("You are not the owner of this account", e.getMessage());
        }

        verify(accountRepository, times(0)).disableAccount(any());
        verify(accountRepository, times(0)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testDisableAccountWhenUserIsOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(true);
        when(accountRepository.isActive(any())).thenReturn(false);
        TransactionDTO msg = accountService.disableAccount(any(), any());

        assertEquals("The account was disabled", msg.getMessage());
        verify(accountRepository, times(1)).disableAccount(any());
        verify(accountRepository, times(1)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testDisableAccountWhenUserIsNotOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(false);
        when(accountRepository.isActive(any())).thenReturn(false);

        try {
            accountService.enableAccount(any(), any());
        } catch (CustomException e) {
            assertEquals("You are not the owner of this account", e.getMessage());
        }

        verify(accountRepository, times(0)).disableAccount(any());
        verify(accountRepository, times(0)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testEnableAccountCantBeEnabledButUserIsTheOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(true);
        when(accountRepository.isActive(any())).thenReturn(false);
        TransactionDTO msg = accountService.enableAccount(any(), any());

        assertEquals("The account can't be enabled", msg.getMessage());
        verify(accountRepository, times(1)).enableAccount(any());
        verify(accountRepository, times(1)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testEnableAccountCantBeEnabledButUserIsNotTheOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(false);
        when(accountRepository.isActive(any())).thenReturn(false);

        try {
            accountService.enableAccount(any(), any());
        } catch (CustomException e) {
            assertEquals("You are not the owner of this account", e.getMessage());
        }

        verify(accountRepository, times(0)).enableAccount(any());
        verify(accountRepository, times(0)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testEnableAccountWhenUserIsOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(true);
        when(accountRepository.isActive(any())).thenReturn(true);
        TransactionDTO msg = accountService.enableAccount(any(), any());

        assertEquals("The account was enabled", msg.getMessage());
        verify(accountRepository, times(1)).enableAccount(any());
        verify(accountRepository, times(1)).isActive(any());
    }

    @Test
    public void testEnableAccountWhenUserIsNotOwner() {
        when(accountRepository.isAccountOwner(any(), any())).thenReturn(false);
        when(accountRepository.isActive(any())).thenReturn(true);

        try {
            accountService.enableAccount(any(), any());
        } catch (CustomException e) {
            assertEquals("You are not the owner of this account", e.getMessage());
        }

        verify(accountRepository, times(0)).enableAccount(any());
        verify(accountRepository, times(0)).isActive(any());
        verify(accountRepository, times(1)).isAccountOwner(any(), any());
    }

    @Test
    public void testGenerateAccountNumber() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountDTO());
        verify(accountRepository, times(1)).existsByAccountNumber(any());
    }

    @Test
    public void testGenerateAccountNumberAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.existsByAccountNumber(any())).thenReturn(true, false);
        accountService.save(defaultAccountDTO());
        verify(accountRepository, times(2)).existsByAccountNumber(any());
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
