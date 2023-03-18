package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.mapper.UserMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.servicio.RoleService;
import co.com.icesi.demojpa.servicio.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    private RoleRepository roleRepository;

    private RoleService roleService;

    private AccountRepository accountRepository;

    @BeforeEach
    private void init(){
        accountRepository=mock(AccountRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        roleService =mock(RoleService.class);
        userService = new UserService(userRepository,userMapper, roleService, roleRepository, accountRepository);

    }

    @Test
    public void testCreateUser(){
        IcesiRole role = testRole();
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));
        userService.save(defaultUserCreateDTO());
        IcesiUser icesiUser1 = defaultIcesiUser();
        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        IcesiRole role = testRole();
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Ya hay un usuario con este email",message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneAlreadyExists(){
        IcesiRole role = testRole();
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));
        when(userRepository.findByPhone(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("Ya hay un usuario con este celular",message);
        }
    }

    @Test
    public void testCreateUserSinRol(){
        UserCreateDTO userCreateDTO =defaultUserCreateDTO();
        userCreateDTO.setRoleName("");
        try{
            userService.save(userCreateDTO);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("El usuario no tiene rol",message);
        }
    }

    @Test
    public void testCreateUserWithNonExistingRole(){
        IcesiRole role = testRole();
        UserCreateDTO userCreateDTO =defaultUserCreateDTO();
        userCreateDTO.setRoleName("AAAAA");
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));

        try{
            userService.save(userCreateDTO);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Este rol no existe",message);
        }
    }

    @Test
    public void testAddAccount(){
        IcesiUser user = defaultIcesiUser();
        IcesiAccount account =defaultIcesiAccount();
        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        userService.addAccount(user,account.getAccountNumber());
        assertEquals(user.getAccounts(),userRepository.findById(user.getUserId()).get().getAccounts());
    }

    @Test
    public void testAddAccountWithNoAccount(){
        IcesiUser user = defaultIcesiUser();
        IcesiAccount account =defaultIcesiAccount();
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        try{
            userService.addAccount(user,account.getAccountNumber());
            fail();
        }catch (RuntimeException exception){
            assertEquals("No existe una cuenta con este numero",exception.getMessage());
        }


    }


    private UserCreateDTO defaultUserCreateDTO(){
     return UserCreateDTO.builder()
             .email("5")
             .firstName("John")
             .lastname("Doe")
             .phone("123")
             .password("123")
             .roleName("rol de prueba")
             .build();
    }

    private IcesiRole testRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("rol de prueba")
                .description("Rol de prueba")
                .build();
    }



    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("5")
                .firstName("John")
                .lastname("Doe")
                .phone("123")
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
                .active(true)
                .balance(10000)
                .type("polish emissary")
                .account(IcesiUser.builder()
                        .email("5")
                        .firstName("John")
                        .lastname("Doe")
                        .phone("123")
                        .password("123")
                        .role(IcesiRole.builder()
                                .name("rol de prueba")
                                .description("rol de prueba")
                                .build())
                        .build())
                .build();
    }

}
