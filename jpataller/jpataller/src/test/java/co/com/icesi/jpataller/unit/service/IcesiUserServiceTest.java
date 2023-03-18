package co.com.icesi.jpataller.unit.service;

import co.com.icesi.jpataller.dto.IcesiRoleDTO;
import co.com.icesi.jpataller.dto.IcesiUserDTO;
import co.com.icesi.jpataller.mapper.IcesiRoleMapperImpl;
import co.com.icesi.jpataller.mapper.IcesiUserMapper;
import co.com.icesi.jpataller.mapper.IcesiUserMapperImpl;
import co.com.icesi.jpataller.model.IcesiRole;
import co.com.icesi.jpataller.model.IcesiUser;
import co.com.icesi.jpataller.repository.IcesiAccountRepository;
import co.com.icesi.jpataller.repository.IcesiRoleRepository;
import co.com.icesi.jpataller.repository.IcesiUserRepository;
import co.com.icesi.jpataller.service.IcesiRoleService;
import co.com.icesi.jpataller.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class IcesiUserServiceTest {

    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    private IcesiUserService icesiUserService;

    private IcesiRoleRepository icesiRoleRepository;

    private IcesiAccountRepository icesiAccountRepository;

    private IcesiUserDTO defaultIcesiUserDTO(){
        return IcesiUserDTO.builder()
                .firstName("Gabriel")
                .lastName("Delgado")
                .email("gabriel.delgado1@u.icesi.edu.co")
                .phoneNumber("318764332")
                .password("sape")
                .roleName("Rol 1")
                .build();
    }

    private IcesiUserDTO IcesiUserDTONoRole(){
        return IcesiUserDTO.builder()
                .firstName("Felipe")
                .lastName("Barreto")
                .email("pipe.barreto@outlook.com")
                .phoneNumber("314542478")
                .password("sape")
                .build();
    }


    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Gabriel")
                .lastName("Delgado")
                .email("gabriel.delgado1@u.icesi.edu.co")
                .phoneNumber("318764332")
                .password("sape")
                .role(defaultIcesiRole())
                .userId(UUID.randomUUID())
                .build();
    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("Rol 1")
                .description("Es un rol de prueba")
                .roleId(UUID.randomUUID())
                .build();
    }

    @BeforeEach
    private void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapperImpl.class);
        icesiUserService = mock(IcesiUserService.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiAccountRepository = mock(IcesiAccountRepository.class);
    }

    @Test
    public void testCreateUser() {
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.createUser(defaultIcesiUserDTO());
        IcesiUser icesiUser = defaultIcesiUser();

        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser)));
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.createUser(defaultIcesiUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User email is in use",message);
        }
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.createUser(defaultIcesiUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User phone number is in use",message);
        }
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.createUser(defaultIcesiUserDTO());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User email and phone number are in use",message);
        }
    }

    @Test
    public void testCreateUserWhenRoleIsNull(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));

        try {
            icesiUserService.createUser(IcesiUserDTONoRole());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("User needs a role",message);
        }
    }



}
