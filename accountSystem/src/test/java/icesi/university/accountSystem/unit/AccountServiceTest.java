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
                .balance(0)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiAccount createDefaultAccount() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(0)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiAccount createDepositAccount() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("DEPOSIT")
                .balance(10000)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiAccount createAccountMoreThan0() {
        return IcesiAccount.builder()
                .active(true)
                .user(createDefaultUser())
                .type("NORMAL")
                .balance(10000)
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


