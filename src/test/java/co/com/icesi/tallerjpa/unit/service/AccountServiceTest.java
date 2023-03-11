package co.com.icesi.tallerjpa.unit.service;

import co.com.icesi.tallerjpa.dto.AccountDTO;
import co.com.icesi.tallerjpa.enums.TypeAccount;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.mapper.AccountMapperImpl;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.service.AccountService;
import co.com.icesi.tallerjpa.strategy.accounts.interfaces.TypeAccountStrategy;
import co.com.icesi.tallerjpa.unit.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
        typeAccountStrategies = mock(List.class);

        accountService = new AccountService(accountRepository, accountMapper, userRepository, typeAccountStrategies);
    }

    @Test
    public void testCreateAccount() {
        accountService.save(defaultAccountDTO());

        verify(accountMapper, times(1)).fromAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(defaultAccount())));
    }

    private AccountDTO defaultAccountDTO() {
        return AccountDTO.builder()
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .active(true)
                .build();
    }

    private Account defaultAccount() {
        return Account.builder()
                .balance(100L)
                .type(TypeAccount.DEPOSIT_ONLY)
                .active(true)
                .build();
    }
}
