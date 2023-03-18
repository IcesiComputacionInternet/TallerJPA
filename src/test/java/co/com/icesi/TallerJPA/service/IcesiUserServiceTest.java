package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapperImpl;
import co.com.icesi.TallerJPA.matcher.IcesiRoleMatcher;
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
    private IcesiUserService service;
    private IcesiUserRepository repository;

    private IcesiUserMapper mapper;
    private IcesiRoleRepository roleRepository;

    @BeforeEach
    private void init(){
        repository = mock(IcesiUserRepository.class);
        roleRepository = mock(IcesiRoleRepository.class);
        mapper = spy(IcesiUserMapperImpl.class);
        service = new IcesiUserService(repository,roleRepository,mapper);
    }


    @Test
    public void createUser(){
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultRole()));
        service.save(defaultDTO());
        verify(roleRepository,times(2)).findByName(any());
        verify(repository,times(1)).save(argThat(new IcesiUserMatcher(defaultUser())));
        verify(roleRepository,times(1)).save(any());
        verify(mapper,times(1)).fromUserDto(any());
    }

    @Test
    public void createUserWhenEmailAndPhoneAlreadyExist(){
        when(repository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        when(repository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            service.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("This email and phone  already exist",exceptionMessage);
            verify(repository, times(0)).save(any());
        }
    }

    @Test
    public void createUserWhenEmailAlreadyExist(){
        when(repository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            service.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("Email already exist",exceptionMessage);
        }
    }

    @Test
    public void createUserWhenPhoneAlreadyExist(){
        when(repository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultUser()));
        try {
            service.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("Phone already exist",exceptionMessage);
        }
    }

    @Test
    public void createUserWhenRoleIsNull(){
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        try {
            service.save(defaultDTO());
            fail();
        }catch (Exception e){
            String exceptionMessage = e.getMessage();
            assertEquals("This role doesn't exist or is null",exceptionMessage);
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
