package com.example.demo.unit.service;

import com.example.demo.DTO.IcesiAccountDTO;
import com.example.demo.Repository.IcesiAccountRepository;
import com.example.demo.model.IcesiAccount;
import com.example.demo.service.IcesiAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    @Mock
    private IcesiAccountRepository accountRepository;

    @InjectMocks
    private IcesiAccountService accountService;

    private IcesiAccountDTO accountDTO;

    private IcesiAccount account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        accountDTO = IcesiAccountDTO.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-01")
                .balance((long) 1000)
                .type("Checking")
                .build();
        account = IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber("123-456789-01")
                .balance((long) 1000)
                .type("Checking")
                .build();
    }

    @Test
    public void createIcesiAccount_InvalidBalance() {
        //Test 2: invalid balance value
        accountDTO.setBalance(-1l);
        assertThrows(IllegalArgumentException.class, () -> accountService.createIcesiAccount(accountDTO));
    }

    @Test
    public void createIcesiAccount_NotNull() {

        when(accountRepository.save(any(IcesiAccount.class))).thenReturn(account);

        IcesiAccount newAccount = accountService.createIcesiAccount(accountDTO);

        assertNotNull(newAccount);
    }
}
