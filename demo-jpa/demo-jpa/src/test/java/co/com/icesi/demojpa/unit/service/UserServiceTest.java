package co.com.icesi.demojpa.unit.service;

import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import co.com.icesi.demojpa.enums.IcesiAccountType;
import co.com.icesi.demojpa.mapper.UserMapper;
import co.com.icesi.demojpa.mapper.UserMapperImpl;
import co.com.icesi.demojpa.model.IcesiAccount;
import co.com.icesi.demojpa.model.IcesiRole;
import co.com.icesi.demojpa.model.IcesiUser;
import co.com.icesi.demojpa.repository.AccountRepository;
import co.com.icesi.demojpa.repository.RoleRepository;
import co.com.icesi.demojpa.repository.UserRepository;
import co.com.icesi.demojpa.service.UserService;
import co.com.icesi.demojpa.unit.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;
    private AccountRepository accountRepository;
    private UserService userService;

    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        roleRepository = mock(RoleRepository.class);
        accountRepository = mock(AccountRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userService = new UserService(userRepository,userMapper,roleRepository, accountRepository);
    }

    @Test
    public void testSave() {
        IcesiRole role = defaultRole();
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));
        userService.save(defaultUserCreateDTO());
        IcesiUser user = defaultIcesiUser();
        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(user)));
    }

    @Test
    public void testSaveWhenEmailAlreadyExists(){
        IcesiRole role = defaultRole();
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message = exception.getMessage();
            assertEquals("User email is already in use",message);
        }
    }

    @Test
    public void testCreateUserWithNonExistingRole(){
        IcesiRole role = defaultRole();
        UserCreateDTO userCreateDTO =defaultUserCreateDTO();
        userCreateDTO.setRoleName("owner");
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of((role)));

        try{
            userService.save(userCreateDTO);
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Role doesn't exists",message);
        }
    }



    private IcesiAccount defaultIcesiAccount(){
        return IcesiAccount.builder()
                .isActive(true)
                .balance(5000L)
                .type(IcesiAccountType.DEFAULT)
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

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("investor")
                .description("test role")
                .build();
    }

    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .email("5")
                .firstName("John")
                .lastName("Doe")
                .password("123")
                .roleName("investor")
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .email("5")
                .firstName("John")
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
}
