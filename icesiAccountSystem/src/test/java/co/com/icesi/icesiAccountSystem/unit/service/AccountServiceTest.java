package co.com.icesi.icesiAccountSystem.unit.service;

import co.com.icesi.icesiAccountSystem.dto.AccountDTO;
import co.com.icesi.icesiAccountSystem.mapper.AccountMapper;
import co.com.icesi.icesiAccountSystem.mapper.AccountMapperImpl;
import co.com.icesi.icesiAccountSystem.model.IcesiAccount;
import co.com.icesi.icesiAccountSystem.model.IcesiUser;
import co.com.icesi.icesiAccountSystem.repository.AccountRepository;
import co.com.icesi.icesiAccountSystem.repository.UserRepository;
import co.com.icesi.icesiAccountSystem.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountMapper accountMapper;

    @BeforeEach
    private void init(){
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper=spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository, userRepository, accountMapper);
    }

    @Test
    public void testCreateAccount(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.saveAccount(defaultAccountDTO());
        IcesiAccount newIcesiAccount = defaultIcesiAccount();
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(newIcesiAccount)));
    }

    @Test
    public void testAccountNumber(){
        userRepository.save(defaultIcesiUser());
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.saveAccount(defaultAccountDTO());
        String accNumPattern = "[0-9]{3}-[0-9]{6}-[0-9]{2}";
        verify(accountRepository,times(1)).save(argThat(acc -> acc.getAccountNumber().matches(accNumPattern)));
    }

    @Test
    public void testCreateAccountWithBalanceBelowCero(){
        try {
            accountService.saveAccount(accountDTOWithBelowCeroBalance());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Account's balance can't be below 0.", message);
        }
    }

    @Test
    public void testCreateDisableAccount(){
        try {
            accountService.saveAccount(disableAccountDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("The status of a new account cannot be disabled.", message);
        }
    }

    @Test
    public void testCreateAccountWithoutUser(){
        try {
            accountService.saveAccount(accountDTOWithoutUser());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("It is not possible to create an account without user.", message);
        }
    }

    @Test
    public void testCreateAccountWithUserDoesNotExists(){
        try {
            accountService.saveAccount(defaultAccountDTO());
            fail();
        } catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("User does not exists.", message);
        }
    }

    private AccountDTO defaultAccountDTO() {
        return AccountDTO.builder()
                .userEmail(defaultIcesiUser().getEmail())
                .balance(500000000)
                .type("deposit only")
                .active(true)
                .build();
    }

    private AccountDTO accountDTOWithBelowCeroBalance() {
        return AccountDTO.builder()
                .userEmail(defaultIcesiUser().getEmail())
                .balance(-100000)
                .type("deposit only")
                .active(true)
                .build();
    }

    private AccountDTO disableAccountDTO() {
        return AccountDTO.builder()
                .userEmail(defaultIcesiUser().getEmail())
                .balance(500000000)
                .type("deposit only")
                .active(false)
                .build();
    }

    private AccountDTO accountDTOWithoutUser() {
        return AccountDTO.builder()
                .userEmail("")
                .balance(500000000)
                .type("deposit only")
                .active(true)
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .user(defaultIcesiUser())
                .balance(500000000)
                .type("deposit only")
                .active(true)
                .build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Damiano")
                .lastName("David")
                .email("ykaar@gmail.com")
                .phoneNumber("3152485689")
                .password("taEkbs08")
                .build();
    }
}
