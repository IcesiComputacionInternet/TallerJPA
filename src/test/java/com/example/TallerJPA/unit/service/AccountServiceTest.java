package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.dto.AccountCreateDTO;
import com.example.TallerJPA.dto.TransactionAccountDTO;
import com.example.TallerJPA.dto.TransferRequestDTO;
import com.example.TallerJPA.mapper.AccountMapper;
import com.example.TallerJPA.mapper.AccountMapperImpl;
import com.example.TallerJPA.mapper.UserMapper;
import com.example.TallerJPA.model.IcesiAccount;
import com.example.TallerJPA.model.IcesiUser;
import com.example.TallerJPA.repository.AccountRepository;
import com.example.TallerJPA.repository.UserRepository;
import com.example.TallerJPA.service.AccountService;
import com.example.TallerJPA.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountMapper accountMapper;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    @BeforeEach
    private void init() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
    }

    @Test
    public void testCreateAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());
        IcesiAccount account = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .active(true)
                .user(defaultIcesiUser())
                .type("Ahorros")
                .balance(0)
                .accountNumber("123-456789-10")
                .build();
        verify(accountRepository, times(1)).save(argThat(new IcesiAccountMatcher(account)));
    }

    @Test
    public void testCreateAccountWhenUserDoesNotExist() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> accountService.save(defaultAccountCreateDTO()));
    }
    private IcesiAccount defaultIcesiAccount() {
        return IcesiAccount.builder()
                .active(true)
                .user(defaultIcesiUser())
                .type("Ahorros")
                .balance(0)
                .accountNumber("123-456789-10")
                .build();
    }

    private IcesiUser defaultIcesiUser() {
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .password("1234")
                .phoneNumber("123456789").build();
    }


    @Test
    public void testChangeAccountStatusWhenBalanceIsZero() {
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        accountService.disableAccount(account.getAccountNumber());
        assertEquals(false, account.getActive());
    }

    @Test
    public void testChangeAccountStatusWhenBalanceIsNotZero() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        assertThrows(RuntimeException.class, () -> accountService.disableAccount(account.getAccountNumber()));
    }
    @Test
    public void testWithdrawWhenBalanceIsZero() {
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        assertThrows(RuntimeException.class, () -> accountService.withdraw(new TransactionAccountDTO(account.getAccountNumber(), 1000)));
    }
    @Test
    public void testWithdrawWhenBalanceIsNotZero() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        accountService.withdraw(new TransactionAccountDTO(account.getAccountNumber(), 500));
        assertEquals(500, account.getBalance());
    }
    @Test
    public void testDepositWhenAccountIsActive() {
        IcesiAccount account = defaultIcesiAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        accountService.deposit(new TransactionAccountDTO(account.getAccountNumber(), 1000));
        assertEquals(1000, account.getBalance());
    }
    @Test
    public void testDepositWhenAccountIsNotActive() {
        IcesiAccount account = defaultIcesiAccount();
        account.setActive(false);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        assertThrows(RuntimeException.class, () -> accountService.deposit(new TransactionAccountDTO(account.getAccountNumber(), 1000)));
    }
    @Test
    public void testTransferWhenBalanceIsZero() {
        IcesiAccount account = defaultIcesiAccount();
        IcesiAccount account2 = defaultIcesiAccount();
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber(account2.getAccountNumber())).thenReturn(Optional.of(account2));
        assertThrows(RuntimeException.class, () -> accountService.transfer(new TransferRequestDTO(account.getAccountNumber(), account2.getAccountNumber(), 1000)));
    }

    @Test
    public void testTransferWhenBalanceIsNotZero() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        IcesiAccount account2 = defaultIcesiAccount();
        account2.setAccountNumber("123-456789-11");
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber(account2.getAccountNumber())).thenReturn(Optional.of(account2));
        accountService.transfer(new TransferRequestDTO(account.getAccountNumber(), account2.getAccountNumber(), 500));
        assertEquals(500, account.getBalance());
        assertEquals(500, account2.getBalance());
    }

    @Test
    public void testTransferToDepositAccount(){
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        IcesiAccount account2 = defaultIcesiAccount();
        account2.setAccountNumber("123-456789-11");
        account2.setType("DEPOSIT_ONLY");
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(accountRepository.findByAccountNumber(account2.getAccountNumber())).thenReturn(Optional.of(account2));
        assertThrows(RuntimeException.class, () -> accountService.transfer(new TransferRequestDTO(account.getAccountNumber(), account2.getAccountNumber(), 500)));
    }


    private AccountCreateDTO defaultAccountCreateDTO() {
        return AccountCreateDTO.builder()
                .userEmail("test@example.com")
                .type("Ahorros").build();
    }
}
