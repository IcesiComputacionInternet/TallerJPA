package icesi.university.accountSystem.unit;

import icesi.university.accountSystem.dto.RequestAccountDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import icesi.university.accountSystem.enums.TypeAccount;
import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import icesi.university.accountSystem.mapper.IcesiAccountMapperImpl;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import icesi.university.accountSystem.security.IcesiSecurityContext;
import icesi.university.accountSystem.services.AccountService;
import icesi.university.accountSystem.services.UserService;
import icesi.university.accountSystem.unit.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;
    private IcesiAccountRepository accountRepository;
    private IcesiAccountMapper accountMapper;
    private IcesiUserRepository userRepository;

    @BeforeEach
    public void setup() {
        accountRepository = mock(IcesiAccountRepository.class);
        accountMapper = spy(IcesiAccountMapperImpl.class);
        userRepository = mock(IcesiUserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
    }

    @Test
    public void testCreateAccount() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(defaultAdmin()));
        accountService.save(defaultAccountDTO());

        verify(userRepository, times(1)).findByEmail(any());
        verify(accountMapper, times(1)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(defaultAccount())));
    }
    @Test
    public void testCreateAccountWithUserNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.empty());
        try {
            accountService.save(defaultAccountDTO());
        } catch (RuntimeException e) {
            assertEquals("User doesn't exists", e.getMessage());
        }

        verify(userRepository, times(1)).findByEmail(any());
        verify(accountMapper, times(0)).fromIcesiAccountDTO(any());
        verify(accountRepository, times(0)).save(argThat(new AccountMatcher(defaultAccount())));
    }

    @Test
    public void testGetAccountByAccountNumber() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(defaultAccount()));

        IcesiAccount account = accountService.getAccountByAccountNumber("12345");

        assertEquals(defaultAccount().getAccountNumber(), account.getAccountNumber());
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
        IcesiAccount account = defaultAccount();
        IcesiUser user =defaultUser();
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));

        accountService.withdrawal(transactionOperationDTO());

        assertEquals(95L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(accountRepository, times(1)).updateBalance(any(), any());
    }

    @Test
    public void testWithdrawAccountNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.empty());

        try {
            accountService.withdrawal(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());

    }

    @Test
    public void testWithdrawAmountGreaterThanBalance() {
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        when(accountRepository.findById(any())).thenReturn(java.util.Optional.of(account));
        try {
            accountService.withdrawal(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("Insufficient funds", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testWithdrawAccountIsNotActive() {
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        account.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        try {
            accountService.withdrawal(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("The account 12345 is not active", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testDeposit() {
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        accountService.deposit(transactionOperationDTO());

        assertEquals(105L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
        verify(accountRepository, times(1)).updateBalance(any(), any());
    }

    @Test
    public void testDepositAccountNotFound() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.empty());

        try {
            accountService.deposit(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("Account not found", e.getMessage());
        }

        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testDepositAccountIsNotActive() {
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        account.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        try {
            accountService.deposit(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("The account 12345 is not active", e.getMessage());
        }

        assertEquals(100L, account.getBalance());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testTransfer() {
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        IcesiAccount account2 = IcesiAccount.builder()
                .balance(100L)
                .active(true)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(java.util.Optional.of(account2));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        accountService.transfer(transactionOperationDTO());

        assertEquals(95L, account.getBalance());
        assertEquals(105L, account2.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(2)).updateBalance(any(), any());
    }

    @Test
    public void testTransferAccountIsNotActive() {
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        IcesiAccount account2 = IcesiAccount.builder()
                .balance(100L)
                .active(false)
                .type(TypeAccount.ACCOUNT_NORMAL)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(java.util.Optional.of(account2));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        try {
            accountService.transfer(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("The account 54321 is not active", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testTransferAccount2IsNotReceiver(){
        IcesiAccount account = defaultAccount();
        IcesiUser user = defaultUser();
        account.setType(TypeAccount.ACCOUNT_NORMAL);
        IcesiAccount account2 = IcesiAccount.builder()
                .balance(100L)
                .active(true)
                .type(TypeAccount.DEPOSIT_ONLY)
                .accountNumber("54321")
                .build();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(java.util.Optional.of(account));
        when(accountRepository.findByAccountNumber("54321")).thenReturn(java.util.Optional.of(account2));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        try {
            accountService.transfer(transactionOperationDTO());
        } catch (RuntimeException e) {
            assertEquals("The account type does not allow transfers", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testTransferWithNoAccountNotAccepted() {
        IcesiAccount account = defaultAccount();
        IcesiAccount account2 = defaultAccount2();
        IcesiUser user = defaultUser();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account2));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));

        try {
            accountService.transfer(transactionOperationDTO());
        } catch (UnsupportedOperationException e) {
            assertEquals("This account type does not transfer", e.getMessage());
        }

        assertEquals(account.getBalance(), account.getBalance());
        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(0)).updateBalance(any(), any());
    }

    @Test
    public void testDisableAccountCantBeDisabled() {
        IcesiUser user = defaultUser();
        IcesiAccount account = defaultAccount();
        when(accountRepository.isActive(any())).thenReturn(true);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        String msg = accountService.deactivateAccount(any());


        assertEquals("The account can't be disabled", msg);
        verify(accountRepository, times(1)).deactivateAccount(any());
        verify(accountRepository, times(1)).isActive(any());
    }

    @Test
    public void testDisableAccount() {
        IcesiUser user = defaultUser();
        IcesiAccount account = defaultAccount();
        when(accountRepository.isActive(any())).thenReturn(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        String msg = accountService.deactivateAccount(any());

        assertEquals("The account was disabled", msg);
        verify(accountRepository, times(1)).deactivateAccount(any());
        verify(accountRepository, times(1)).isActive(any());
    }

    @Test
    public void testEnableAccountCantBeEnabled() {
        IcesiUser user = defaultUser();
        when(accountRepository.isActive(any())).thenReturn(false);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(defaultAccount()));
        String msg = accountService.activateAccount(any());

        assertEquals("The account can't be enabled", msg);
        verify(accountRepository, times(1)).activateAccount(any());
        verify(accountRepository, times(1)).isActive(any());
    }

    @Test
    public void testEnableAccount() {
        IcesiUser user = defaultAdmin();
        IcesiAccount account = defaultAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(java.util.Optional.of(account));
        when(accountRepository.isActive(any())).thenReturn(true);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        String msg = accountService.activateAccount("12345");

        assertEquals("The account was enabled", msg);
        verify(accountRepository, times(1)).activateAccount(any());
        verify(accountRepository, times(1)).isActive(any());
    }

    @Test
    public void testGenerateAccountNumber() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(defaultAdmin()));
        accountService.save(defaultAccountDTO());
        verify(accountRepository, times(1)).existsByAccountNumber(any());
    }

    @Test
    public void testGenerateAccountNumberAlreadyExists() {
        when(userRepository.findByEmail(any())).thenReturn(java.util.Optional.of(defaultUser()));
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

    private TransactionOperationDTO transactionOperationDTO(){
        return TransactionOperationDTO.builder()
                .accountFrom("12345")
                .accountTo("54321")
                .amount(5L)
                .build();
    }

    private IcesiAccount defaultAccount() {
        return IcesiAccount.builder()
                .accountNumber("12345")
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .active(true)
                .user(defaultUser())
                .build();
    }

    private IcesiAccount defaultAccount2() {
        return IcesiAccount.builder()
                .accountNumber("54321")
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .active(true)
                .user(defaultUser())
                .build();
    }

    private IcesiUser defaultAdmin(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role(adminRole())
                .build();
    }

    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role(userRole())
                .build();
    }

    private IcesiUser defaultBank(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .firstName("Arturo")
                .lastName("Diaz")
                .email("prueba@gmail.com")
                .phoneNumber("12345")
                .password("12345")
                .role(bankRole())
                .build();
    }



    private IcesiRole adminRole(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .name("ADMIN")
                .description("ADMIN")
                .build();
    }

    private IcesiRole userRole(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .name("USER")
                .description("USER")
                .build();
    }

    private IcesiRole bankRole(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a80101-0000-0000-0000-000000000000"))
                .name("BANK")
                .description("BANK")
                .build();
    }
}



