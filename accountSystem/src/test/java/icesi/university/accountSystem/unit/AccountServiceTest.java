package icesi.university.accountSystem.unit;

import icesi.university.accountSystem.dto.IcesiAccountDTO;
import icesi.university.accountSystem.mapper.IcesiAccountMapper;
import icesi.university.accountSystem.mapper.IcesiAccountMapperImpl;
import icesi.university.accountSystem.model.IcesiAccount;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiAccountRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import icesi.university.accountSystem.services.AccountService;
import icesi.university.accountSystem.services.UserService;
import icesi.university.accountSystem.unit.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private IcesiAccountRepository accountRepository;

    private IcesiAccountMapper accountMapper;

    private IcesiUserRepository userRepository;

    private AccountService accountService;

    private UserService userService;

    @BeforeEach
    private void init() {
        accountRepository = mock(IcesiAccountRepository.class);
        accountMapper = spy(IcesiAccountMapperImpl.class);
        userService = mock(UserService.class);
        userRepository = mock(IcesiUserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
    }

    @Test
    public void saveAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultUser()));
        IcesiAccount account = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(0)
                .accountNumber("123-456789-10")
                .build();
        accountRepository.save(account);
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(account)));
    }
    @Test
    public void withdrawal() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultAccount()));
        when(accountRepository.save(any())).thenReturn(createDefaultAccount());

        IcesiAccount icesiAccount = accountRepository.findByAccountNumber(createDefaultAccount().getAccountNumber()).get();
        accountService.withdrawal(icesiAccount.getAccountNumber(), 100L);

        verify(accountRepository, atLeast(1)).findByAccountNumber(any());
        verify(accountRepository, atLeast(1)).save(any());
        assertEquals(icesiAccount.getBalance(), 900L);
    }
    @Test
    public void deposit() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultAccount()));
        when(accountRepository.save(any())).thenReturn(createDefaultAccount());
        IcesiAccount icesiAccount = accountRepository.findByAccountNumber(createDefaultAccount().getAccountNumber()).get();
        accountService.deposit(icesiAccount.getAccountNumber(), 5000L);

        verify(accountRepository, times(2)).findByAccountNumber(any());
        verify(accountRepository, times(1)).save(any());
        assertEquals(icesiAccount.getBalance(), 6000L);
    }

    @Test
    public void transfer() {
        when(accountRepository.findByAccountNumber(createDefaultAccount().getAccountNumber())).thenReturn(Optional.ofNullable(createDefaultAccount()));
        when(accountRepository.findByAccountNumber(createAnotherAccount().getAccountNumber())).thenReturn(Optional.ofNullable(createAnotherAccount()));
        when(accountRepository.save(any())).thenReturn(createDepositAccount());

        IcesiAccount icesiAccount = accountRepository.findByAccountNumber(createDefaultAccount().getAccountNumber()).get();
        IcesiAccount icesiAccount2 = accountRepository.findByAccountNumber(createAnotherAccount().getAccountNumber()).get();
        accountService.transfer(icesiAccount.getAccountNumber(),icesiAccount2.getAccountNumber() ,100L);

        verify(accountRepository, times(2)).save(any());

        assertEquals(100L,icesiAccount2.getBalance());
    }

    @Test
    public void deactivateAccountWithBalanceDifferent0() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultAccount()));
        when(accountRepository.save(any())).thenReturn(createDefaultAccount());

        IcesiAccount icesiAccount = createDefaultAccount();
        assertThrows(RuntimeException.class,()->accountService.deactivateAccount(icesiAccount.getAccountNumber()));
        verify(accountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void activateAccountAlreadyActivate() {
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDefaultAccount()));
        when(accountRepository.save(any())).thenReturn(createDefaultAccount());

        IcesiAccount icesiAccount = createDefaultAccount();

        assertThrows(RuntimeException.class,()->accountService.activateAccount(icesiAccount.getAccountNumber()));
        verify(accountRepository, times(2)).findByAccountNumber(any());
    }

    @Test
    public void saveWithbelow0(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultUser()));
        IcesiAccount account = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(0)
                .accountNumber("123-456789-10")
                .build();
        try{ accountRepository.save(account);}
        catch (RuntimeException e){
            assertEquals("The balance of account is below 0",e.getMessage());
        }

        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(account)));
    }

    @Test
    public void disableAccountWithout0(){
        when(accountRepository.save(any())).thenReturn(createAccountMoreThan0());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createAccountMoreThan0()));

        try {
            accountRepository.save(any());
            accountService.deactivateAccount(createAccountMoreThan0().getAccountNumber());}
        catch (RuntimeException e){
            assertEquals("Account balance is different from 0",e.getMessage());
        }
    }

    @Test
    public void transferWithDepositAccount(){
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.ofNullable(createDepositAccount()));
        when(accountRepository.save(any())).thenReturn(createDefaultAccount());

        try {
            IcesiAccount depositAccount = accountRepository.findByAccountNumber(createDefaultAccount().getAccountNumber()).get();
            IcesiAccount defaultAccount = accountRepository.save(any());
            accountService.transfer(defaultAccount.getAccountNumber(),depositAccount.getAccountNumber(),5000);
            }
        catch (RuntimeException e){
            assertEquals("One of accounts is deposit only",e.getMessage());
        }
    }


    private IcesiAccountDTO createDefaultAccountDTO() {
        return IcesiAccountDTO.builder()
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(1000L)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiAccount createDefaultAccount() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(1000L)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiAccount createAnotherAccount() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(0L)
                .accountNumber("123-456789-11")
                .build();
    }

    private IcesiAccount createDepositAccount() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("DEPOSIT")
                .balance(10000L)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiAccount createAccountMoreThan0() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(10000L)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiUser createDefaultUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("312545454")
                .password("12456789")
                .email("testEmail@example.com")
                .role(createDefaultRole())
                .build();
    }
    private IcesiRole createDefaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Role")
                .description("this is a role")
                .icesiUsers(new ArrayList<>())
                .build();
    }
}


