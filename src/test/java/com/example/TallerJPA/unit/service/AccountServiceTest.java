/**package com.example.TallerJPA.unit.service;

import com.example.TallerJPA.dto.AccountCreateDTO;
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
    private UserService userService;

    @BeforeEach
    private void init() {
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userService = mock(UserService.class);
        accountService = new AccountService(accountRepository, accountMapper, userService);
    }

    @Test
    public void testCreateAccount() {
        when(userService.findUserByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
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
        when(userService.findUserByEmail(any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> accountService.save(defaultAccountCreateDTO()));
    }

    @Test
    public void testChangeAccountStatusWhenBalanceIsZero() {
        IcesiAccount account = defaultIcesiAccount();
        accountService.changeStatus(account);
        assertEquals(false, account.getActive());
    }
    @Test
    public void testChangeAccountStatusWhenBalanceIsNotZero() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        accountService.changeStatus(account);
        assertEquals(true, account.getActive());
    }
    @Test
    public void testWithdrawWhenBalanceIsZero() {
        IcesiAccount account = defaultIcesiAccount();
        assertThrows(RuntimeException.class, () -> accountService.withdraw(account, 1000));
    }
    @Test
    public void testWithdrawWhenBalanceIsNotZero() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        accountService.withdraw(account, 500);
        assertEquals(500, account.getBalance());
    }
    @Test
    public void testWithdrawWhenBalanceIsNotEnough() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        assertThrows(RuntimeException.class, () -> accountService.withdraw(account, 2000));
    }
    @Test
    public void testDeposit() {
        IcesiAccount account = defaultIcesiAccount();
        accountService.deposit(account, 1000);
        assertEquals(1000, account.getBalance());
    }
    @Test
    public void testTransferWhenBalanceIsZero() {
        IcesiAccount account = defaultIcesiAccount();
        IcesiAccount account2 = defaultIcesiAccount();
        account2.setAccountId(UUID.randomUUID());
        assertThrows(RuntimeException.class, () -> accountService.transfer(account, account2, 1000));
    }
    @Test
    public void testTransferWhenBalanceIsNotZero() {
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        IcesiAccount account2 = defaultIcesiAccount();
        account2.setAccountId(UUID.randomUUID());
        accountService.transfer(account, account2, 500);
        assertEquals(500, account.getBalance());
        assertEquals(500, account2.getBalance());
    }
    @Test
    public void testTransferToDepositAccount(){
        IcesiAccount account = defaultIcesiAccount();
        account.setBalance(1000);
        IcesiAccount account2 = defaultIcesiAccount();
        account2.setAccountId(UUID.randomUUID());
        account2.setType("Deposito");
        assertThrows(RuntimeException.class, () -> accountService.transfer(account, account2, 500));
    }


    private AccountCreateDTO defaultAccountCreateDTO() {
        return AccountCreateDTO.builder()
                .userEmail("test@example.com")
                .type("Ahorros").build();
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
}
**/