package co.com.icesi.tallerjpa.unit.service;


import co.com.icesi.tallerjpa.Enum.AccountType;
import co.com.icesi.tallerjpa.dto.AccountCreateDTO;
import co.com.icesi.tallerjpa.dto.RoleCreateDTO;
import co.com.icesi.tallerjpa.dto.UserCreateDTO;
import co.com.icesi.tallerjpa.mapper.AccountMapper;
import co.com.icesi.tallerjpa.mapper.AccountMapperImpl;
import co.com.icesi.tallerjpa.model.IcesiAccount;
import co.com.icesi.tallerjpa.model.IcesiRole;
import co.com.icesi.tallerjpa.model.IcesiUser;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import co.com.icesi.tallerjpa.repository.UserRepository;
import co.com.icesi.tallerjpa.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private AccountMapper accountMapper;
    private UserRepository userRepository;

    @BeforeEach
    private void init(){
        accountRepository = mock(AccountRepository.class);
        accountMapper = spy(AccountMapperImpl.class);
        userRepository = mock(UserRepository.class);
        accountService = new AccountService(accountRepository, accountMapper, userRepository);
        userRepository.save(defaultIcesiUser());
    }

   /* @Test
    public void testCreateAccount(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());
        IcesiAccount icesiAccount1 = defaultIcesiAccount();
        verify(accountRepository,times( 1)).save(argThat(new IcesiAccountMatcher(icesiAccount1)));
    }*/

    @Test
    public void checkAccountNumber(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());

        verify(accountRepository,times( 1)).save(argThat(accountNum-> accountNum.getAccountNumber().matches("[0-9]{3}-[0-9]{6}-[0-9]{2}")));
    }

    @Test
    public void testCreateAccountWithBalanceBelowZero(){
        //when(accountMapper.fromIcesiAccountDTO(any())).thenReturn(defaultIcesiAccount());
        try {
            accountService.save(defaultAccountCreateDTOBalanceBelowZero());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("Account balance can't be below 0", messageOfException);
        }
    }

    @Test
    public void testDisableAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTOBalancedNotZero());

        IcesiAccount icesiAccount = accountService.save(defaultAccountCreateDTOBalancedNotZero());

        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));

        verify(accountRepository, times(1)).save(any());

        try {
            accountService.accountState(icesiAccount.getAccountNumber());
            fail();
        } catch (RuntimeException exception) {
            String messageOfException = exception.getMessage();
            assertEquals("State can't be changed", messageOfException);
        }

    }

   /* @Test
    public void testEnabledAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultAccountCreateDisabled());
        accountService.save(defaultAccountCreateDTODisabled());

        String accountNum = defaultAccountCreateDTODisabled().getAccountNumber();

        when(accountRepository.findByAccountNumber(accountNum)).thenReturn(Optional.of(defaultIcesiAccount()));

        //verify(accountRepository, times(1)).save(any());

        accountService.accountState(accountNum);
        assertTrue(defaultAccountCreateDTODisabled().isActive());

    }*/

    @Test
    public void testWithdrawalMoneySuccesfully(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccount = accountService.save(defaultAccountCreateDTO());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.withdrawal(icesiAccount.getAccountNumber(), 20);


        assertEquals(119989, icesiAccount.getBalance());
    }


    private AccountCreateDTO defaultAccountCreateDTO(){
        return AccountCreateDTO.builder()
                .balance(12000)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .icesiUserDto(defaultUserCreateDTO())
                .build();
    }

    private AccountCreateDTO defaultAccountCreateDTOBalancedNotZero(){
        return AccountCreateDTO.builder()
                .balance(10)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .icesiUserDto(defaultUserCreateDTO())
                .build();
    }

    private AccountCreateDTO defaultAccountCreateDTOBalanceBelowZero(){
        return AccountCreateDTO.builder()
                .balance(-200)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .icesiUserDto(defaultUserCreateDTO())
                .build();
    }

    private AccountCreateDTO defaultAccountCreateDTODisabled(){
        return AccountCreateDTO.builder()
                .balance(90)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(false)
                .icesiUserDto(defaultUserCreateDTO())
                .build();
    }

    private IcesiAccount defaultAccountCreateDisabled(){
        return IcesiAccount.builder()
                .accountNumber("123-456789-19")
                .balance(90)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(false)
                .user(defaultIcesiUser())
                .build();
    }

    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .accountNumber("123-456789-10")
                .balance(120009)
                .type(AccountType.DEPOSIT_ONLY.toString())
                .active(true)
                .user(defaultIcesiUser())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .role(defaultIcesiRole())
                .build();
    }

    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .email("testEmail@example.com")
                .phoneNumber("1234567")
                .password("1234")
                .icesiroleDto(defaultRoleCreateDTO())
                .build();
    }

    private RoleCreateDTO defaultRoleCreateDTO() {
        return RoleCreateDTO.builder()
                .description("no description")
                .name("Administrator")
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("no description")
                .name("Administrator")
                .build();
    }



}
