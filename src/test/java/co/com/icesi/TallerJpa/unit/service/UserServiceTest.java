package co.com.icesi.TallerJpa.unit.service;

import co.com.icesi.TallerJpa.dto.IcesiUserCreateDTO;
import co.com.icesi.TallerJpa.exceptions.icesiUserExceptions.RoleCantBeNullException;
import co.com.icesi.TallerJpa.exceptions.icesiUserExceptions.UserAttributeAlreadyInUseException;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapper;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapperImpl;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import co.com.icesi.TallerJpa.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private IcesiUserService icesiUserService;
    private IcesiUserRepository icesiUserRepository;
    private IcesiUserMapper icesiUserMapper;
    @BeforeEach
    public void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapperImpl.class);
        icesiUserService = new IcesiUserService(icesiUserRepository, icesiUserMapper);
    }
    @Test
    public void testCreateUser(){
        icesiUserService.saveUser(defaultUserCreateDTO(), defaultRole());
        IcesiUser icesiUser = IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@test.com")
                .phoneNumber("3201112222")
                .password("12345")
                .build();
        IcesiRole role = IcesiRole.builder()
                .name("ROLE_STUDENT")
                .description("Estudiante de la universidad ICESI")
                .build();
        icesiUser.setIcesiRole(role);
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser)));
    }
    @Test
    public void testCreateUserEmailAndPhoneNumberAlreadyInUse(){
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.saveUser(defaultUserCreateDTO(),defaultRole());
            fail();
        }catch (UserAttributeAlreadyInUseException exception){
            String message = exception.getMessage();
            assertEquals("AttributeAlreadyInUseException: Email and phone number are already in use", message);
        }
    }
    @Test
    public void testCreateUserEmailAlreadyInUse(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.saveUser(defaultUserCreateDTO(), defaultRole());
            fail();
        }catch (UserAttributeAlreadyInUseException exception){
            String message = exception.getMessage();
            assertEquals("AttributeAlreadyInUseException: Email is already in use", message);
        }
    }
    @Test
    public void testCreateUserPhoneNumberAlreadyInUse(){
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.saveUser(defaultUserCreateDTO(), defaultRole());
            fail();
        }catch (UserAttributeAlreadyInUseException exception){
            String message = exception.getMessage();
            assertEquals("AttributeAlreadyInUseException: Phone number is already in use", message);
        }
    }
    @Test
    public void testCreateUserRoleNull(){
        when(icesiUserRepository.findById(any())).thenReturn(Optional.of(defaultIcesiUser()));
        try{
            icesiUserService.saveUser(defaultUserCreateDTO(), null);
            fail();
        }catch (RoleCantBeNullException exception){
            String message = exception.getMessage();
            assertEquals("RoleCantBeNullException: The role in user cant be null", message);
        }
    }
    private IcesiUserCreateDTO defaultUserCreateDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@test.com")
                .phoneNumber("3201112222")
                .password("12345")
                .build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@test.com")
                .phoneNumber("3201112222")
                .password("12345")
                .build();
    }
    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("ROLE_STUDENT")
                .description("Estudiante de la universidad ICESI")
                .build();
    }
}
