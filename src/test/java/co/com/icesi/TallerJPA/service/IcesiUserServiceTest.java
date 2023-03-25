package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapperImpl;
import co.com.icesi.TallerJPA.matcher.IcesiUserMatcher;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {
    private IcesiUserService userService;
    private IcesiUserRepository userRepository;

    private IcesiUserMapper userMapper;
    private IcesiRoleRepository roleRepository;

    @BeforeEach
    public void init(){
        userRepository = mock(IcesiUserRepository.class);
        roleRepository = mock(IcesiRoleRepository.class);
        userMapper = spy(IcesiUserMapperImpl.class);
        userService = new IcesiUserService(userRepository,roleRepository, userMapper);
    }


    @Test
    public void createUser(){
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultRole()));
        userService.save(defaultDTO());
        verify(roleRepository,times(1)).findByName(any());
        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(defaultUser())));
        verify(roleRepository,times(1)).save(any());
        verify(userMapper,times(1)).fromUserDto(any());
    }

    @Test
    public void createUserWhenEmailAndPhoneAlreadyExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            userService.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("This email and phone already exist: "+defaultDTO().getEmail()+" and "+defaultDTO().getPhoneNumber(),exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    @Test
    public void createUserWhenEmailAlreadyExist(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            userService.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("This email already exist: "+defaultDTO().getEmail(),exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    @Test
    public void createUserWhenPhoneAlreadyExist(){
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            userService.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("This phone already exist: "+defaultDTO().getPhoneNumber(),exceptionMessage);
            verify(userRepository, times(0)).save(any());
        }
    }

    @Test
    public void createUserWhenRoleDoesNotExist(){
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        try {
            userService.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("This role doesn't exist",exceptionMessage);
        }
    }

    private IcesiUser defaultUser(){
        return IcesiUser.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(defaultRole())
                .build();
    }

    private IcesiUserDTO defaultDTO(){
        return IcesiUserDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("672155121")
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(defaultRoleDTO())
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .users(new ArrayList<>())
                .build();
    }

    private IcesiRoleDTO defaultRoleDTO(){
        return   IcesiRoleDTO.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }

}
