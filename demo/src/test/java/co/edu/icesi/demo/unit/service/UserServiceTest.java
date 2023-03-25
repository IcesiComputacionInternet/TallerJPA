package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.RoleCreateDTO;
import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.mapper.UserMapper;
import co.edu.icesi.demo.mapper.UserMapperImpl;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.RoleRepository;
import co.edu.icesi.demo.repository.UserRepository;
import co.edu.icesi.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    private UserMapper userMapper;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

/*
    @BeforeEach
    private void init(){
        userRepository=mock(UserRepository.class);
        userMapper=spy(UserMapperImpl.class);
        roleRepository=mock(RoleRepository.class);

        userService=new UserService(userRepository, userMapper, roleRepository);

        roleRepository.save(defaultIcesiRole());
    }


    @Test
    public void testCreateUser() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        userService.save(defaultUserCreateDTO());
        IcesiUser icesiUser = defaultIcesiUser();

        verify(userRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser)));
    }

   @Test
    public void testCreateUserWhenEmailAlreadyExists(){
       when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

       when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User email is in use",message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User phone number is in use",message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            userService.save(defaultUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User email and phone number are in use",message);
        }
    }

    @Test
    public void testCreateUserWhenRoleIsNull(){
        when(roleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        try {
            userService.save(nullRoleUserCreateDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User needs a role",message);
        }
    }

    private UserCreateDTO defaultUserCreateDTO(){

        return UserCreateDTO.builder()
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("3184441232")
                .password("julieta123")
                .roleCreateDTO(defaultRoleCreateDTO())
                .build();
    }

    private UserCreateDTO nullRoleUserCreateDTO(){

        return UserCreateDTO.builder()
                .firstName("Julieta")
                .lastName("Venegas")
                .email("julietav@example.com")
                .phoneNumber("3184441232")
                .password("julieta123")
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

    private RoleCreateDTO defaultRoleCreateDTO(){
        return RoleCreateDTO.builder()
                .name("administrator")
                .description("is an administrator")
                .build();
    }


*/
}
