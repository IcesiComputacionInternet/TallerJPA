package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.AccountCreateDTO;
import co.com.icesi.demojpa.mapper.AccountMapper;
import co.com.icesi.demojpa.mapper.AccountMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.AccountService;
import co.com.icesi.demojpa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.Mockito.*;


public class AccountServiceTest {

    private AccountService accountService;
    private AccountMapper accountMapper;
    private AccountRepository accountRepository;

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    private void init(){
        userService =mock(UserService.class);
        userRepository=mock(UserRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        accountRepository = mock(AccountRepository.class);

        accountService = new AccountService(accountRepository, accountMapper, userRepository, userService);
    }

    private AccountCreateDTO defaultAccountDTO(){
        return AccountCreateDTO.builder()
                .userId("")
                .isActive(true)
                .balance(5000)
                .type("inversion")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("5")
                .firstName("Maki")
                .lastName("Doe")
                .phoneNumber("123")
                .password("123")
                .accounts(new ArrayList<>())
                .role(IcesiRole.builder()
                        .name("rol de prueba")
                        .description("rol de prueba")
                        .build())
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .isActive(true)
                .balance(5000)
                .type("polish emissary")
                .user(IcesiUser.builder()
                        .email("5")
                        .firstName("John")
                        .lastName("Doe")
                        .phoneNumber("123")
                        .password("123")
                        .role(IcesiRole.builder()
                                .name("rol de prueba")
                                .description("rol de prueba")
                                .build())
                        .build())
                .build();
    }
    private IcesiAccount defaultIcesiAccountWithNumberAndID(){
        return IcesiAccount.builder()
                .accountId(UUID.randomUUID())
                .accountNumber(accountService.generateRandomCode())
                .isActive(true)
                .balance(5000)
                .type("polish emissary")
                .build();
    }


}
