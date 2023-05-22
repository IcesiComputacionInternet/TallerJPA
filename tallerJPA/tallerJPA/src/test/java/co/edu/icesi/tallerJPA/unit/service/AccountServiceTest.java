package co.edu.icesi.tallerJPA.unit.service;

import co.edu.icesi.tallerJPA.dto.AccountDTO;
import co.edu.icesi.tallerJPA.mapper.AccountMapper;
import co.edu.icesi.tallerJPA.model.Account;
import co.edu.icesi.tallerJPA.repository.AccountRepository;
import co.edu.icesi.tallerJPA.repository.IcesiUserRepository;
import co.edu.icesi.tallerJPA.service.AccountService;
import co.edu.icesi.tallerJPA.unit.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;

    private IcesiUserRepository icesiUserRepository;

    @BeforeEach
    public void init() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapper.class);
        accountService = new AccountService(accountRepository,  accountMapper, icesiUserRepository );
    }

    @Test
    public void testSaveAccount() {
        accountService.save(accountDTOByDefault());

        verify(accountMapper, times(1)).fromAccountDTO(any());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(accountByDefault())));
    }

    private AccountDTO accountDTOByDefault() {
        return AccountDTO.builder()
                .balance(100L)
                .type("default")
                .active(true)
                .build();
    }

    private Account accountByDefault() {
        return Account.builder()
                .balance(100L)
                .type("default")
                .active(true)
                .build();
    }
}
