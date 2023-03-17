package co.com.icesi.demojpa.unit.service;


import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.AccountMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;

import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.servicio.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {

    private AccountMapper accountMapper;

    private AccountService accountService;

    private AccountRepository  accountRepository;

    @BeforeEach
    private void init(){
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountService = new AccountService(accountRepository,accountMapper);
    }

    @Test
    public void testCreateAccount(){
        accountService.save(defaultAccountDTO());
        IcesiAccount icesiAccount = defaultIcesiAccount();
        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    public void testCreateAccountWithNegativeBalance(){
        AccountCreateDTO accountCreateDTO = defaultAccountDTO();
        accountCreateDTO.setBalance(-22222);
        try{
            accountService.save(accountCreateDTO);
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("El balance no puede ser menor a 0",message);
        }
    }

    @Test
    public void testFindByNumber(){
        IcesiAccount account =defaultIcesiAccountWithNumberAndID();
        accountRepository.save(account);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        Optional<IcesiAccount> account2 = accountRepository.findByAccountNumber(account.getAccountNumber());
        assertEquals(account2.get().getAccountNumber(),account.getAccountNumber());
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    public void testDisable(){
        IcesiAccount icesiAccount = defaultIcesiAccountWithNumberAndID();
        accountRepository.save(icesiAccount);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        icesiAccount.setBalance(0);
        accountService.disableAccount(icesiAccount.getAccountNumber());
        assertEquals(false,icesiAccount.isActive());
    }

    private AccountCreateDTO defaultAccountDTO(){
        return AccountCreateDTO.builder()
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .build();
    }
    private IcesiAccount defaultIcesiAccountWithNumberAndID(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber(accountService.genNumber())
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .build();
    }
}


