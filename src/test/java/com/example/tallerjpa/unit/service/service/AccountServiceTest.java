package com.example.tallerjpa.unit.service.service;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.mapper.AccountMapper;
import com.example.tallerjpa.model.IcesiAccount;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.model.IcesiUser;
import com.example.tallerjpa.repository.AccountRepository;
import com.example.tallerjpa.repository.UserRepository;
import com.example.tallerjpa.service.AccountService;
import com.example.tallerjpa.service.UserService;
import com.example.tallerjpa.unit.service.matcher.AccountMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = spy(AccountMapper.class);
        accountService = new AccountService(accountRepository, userRepository, accountMapper);
    }

    @Test
    public void testSaveAccount() {
        when(userRepository.searchByEmail(any())).thenReturn(Optional.of(defaultUser()));
        accountService.createAccount(defaultAccountDTO());
        verify(accountRepository, times(1)).save(argThat(new AccountMatcher(defaultAccount())));
        verify(accountMapper, times(1)).fromAccountDTO(defaultAccountDTO());

    }


    private AccountDTO defaultAccountDTO(){
        return AccountDTO.builder()
                .type("default")
                .active(true)
                .emailUser("juan@hotmail")
                .build();
    }

    private IcesiAccount defaultAccount(){
        return IcesiAccount.builder()
                .type("default")
                .active(true)
                .build();
    }
    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("12345678")
                .password("password")
                .icesiRole(defaultRole())
                .build();
    }

    private UserDTO defaultUserDTO(){
        return UserDTO.builder()
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("12345678")
                .password("password")
                .role("Student")
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("Student")
                .description("Student of the Icesi University")
                .build();
    }

}
