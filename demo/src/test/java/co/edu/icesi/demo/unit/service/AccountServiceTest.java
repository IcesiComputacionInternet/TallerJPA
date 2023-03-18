package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.AccountCreateDTO;
import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.mapper.AccountMapper;
import co.edu.icesi.demo.mapper.AccountMapperImpl;
import co.edu.icesi.demo.model.IcesiAccount;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.AccountRepository;
import co.edu.icesi.demo.repository.UserRepository;
import co.edu.icesi.demo.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountRepository accountRepository;

    private AccountMapper accountMapper;

    private UserRepository userRepository;

    @BeforeEach
    private void init(){
        accountRepository=mock(AccountRepository.class);
        accountMapper=spy(AccountMapperImpl.class);
        userRepository=mock(UserRepository.class);
        accountService=new AccountService(accountRepository,accountMapper,userRepository);

        userRepository.save(defaultIcesiUser());
    }

    @Test
    public void testCreateAccount(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());
        IcesiAccount icesiAccount= defaultIcesiAccount();

        verify(accountRepository,times(1)).save(argThat(new IcesiAccountMatcher(icesiAccount)));
    }

    @Test
    public void testAccountNumberFormat(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        accountService.save(defaultAccountCreateDTO());
        verify(accountRepository,times(1)).save(argThat(a-> a.getAccountNumber().matches("[0-9]{3}-[0-9]{6}-[0-9]{2}")));

    }

    @Test
    public void testCreateAccountWithBalanceBelow0(){
       try{
           accountService.save(accountCreateDTOWithBalanceBelow0());
           fail();
       }catch(RuntimeException exception){
           String message= exception.getMessage();
           assertEquals("Account balance can't be below 0",message);
       }

      }

    @Test
    public void testCreateAccountWithBalanceNotIn0AndDisable(){
        try{
            accountService.save(accountCreateDTOWithBalanceNotIn0AndDisable());
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Account can only be disable if the balance is 0",message);
        }

    }

    @Test
    public void testEnableAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountDisabled());
        IcesiAccount icesiAccount = accountService.save(accountCreateDTODisabled());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.changeState(icesiAccount.getAccountNumber(), true);


        assertTrue(icesiAccount.isActive());

    }

    @Test
    public void testDisableAccount() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(accountToDisable());
        IcesiAccount icesiAccount = accountService.save(accountCreateDTOToDisable());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        accountService.changeState(icesiAccount.getAccountNumber(), false);


        assertFalse(icesiAccount.isActive());

    }

    @Test
    public void testDisableAccountWhenCannotDoIt() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(accountRepository.save(any())).thenReturn(defaultIcesiAccount());
        IcesiAccount icesiAccount = accountService.save(defaultAccountCreateDTO());
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(icesiAccount));
        try {
            accountService.changeState(icesiAccount.getAccountNumber(), false);
            fail();
        }catch(RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Balance is not 0. Account can't be disabled",message);

        }

    }

    @Test
    public void testWithdrawalMoney(){
        
    }



    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .type("normal")
                .active(true)
                .balance(500000)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-12")
                .build();
    }

    private IcesiUser defaultIcesiUser(){

        return IcesiUser.builder()
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("3184441232")
                .password("julieta123")
                .role(defaultIcesiRole())
                .userId(UUID.randomUUID())
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("administrator")
                .description("is an administrator")
                .roleId(UUID.randomUUID())
                .build();
    }
    private AccountCreateDTO defaultAccountCreateDTO(){
        return AccountCreateDTO.builder()
                .type("normal")
                .active(true)
                .balance(500000)
                .userCreateDTO(defaultUserCreateDTO())
                .build();
    }


    private AccountCreateDTO accountCreateDTOWithBalanceNotIn0AndDisable(){
        return AccountCreateDTO.builder()
                .type("normal")
                .active(false)
                .balance(500000)
                .userCreateDTO(defaultUserCreateDTO())
                .build();
    }

    private AccountCreateDTO accountCreateDTOWithBalanceBelow0(){
        return AccountCreateDTO.builder()
                .type("normal")
                .active(true)
                .balance(-1000)
                .userCreateDTO(defaultUserCreateDTO())
                .build();
    }

    private UserCreateDTO defaultUserCreateDTO(){
        RoleCreateDTO roleCreateDTO= defaultRoleCreateDTO();
        return UserCreateDTO.builder()
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("3184441232")
                .password("julieta123")
                .roleCreateDTO(roleCreateDTO)
                .build();
    }

    private AccountCreateDTO accountCreateDTODisabled(){
        return AccountCreateDTO.builder()
                .type("normal")
                .active(false)
                .balance(0)
                .userCreateDTO(defaultUserCreateDTO())
                .build();
    }

    private IcesiAccount accountDisabled(){
        return IcesiAccount.builder()
                .type("normal")
                .active(false)
                .balance(0)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-12")
                .build();
    }

    private AccountCreateDTO accountCreateDTOToDisable(){
        return AccountCreateDTO.builder()
                .type("normal")
                .active(true)
                .balance(0)
                .userCreateDTO(defaultUserCreateDTO())
                .build();
    }

    private IcesiAccount accountToDisable(){
        return IcesiAccount.builder()
                .type("normal")
                .active(true)
                .balance(0)
                .user(defaultIcesiUser())
                .accountNumber("123-123456-12")
                .build();
    }
    private AccountCreateDTO accountCreateDTODepositOnly(){
        return AccountCreateDTO.builder()
                .type("deposit only")
                .active(true)
                .balance(500000)
                .userCreateDTO(defaultUserCreateDTO())
                .build();
    }
    private RoleCreateDTO defaultRoleCreateDTO(){
        return RoleCreateDTO.builder()
                .name("administrator")
                .description("is an administrator")
                .build();
    }


}
