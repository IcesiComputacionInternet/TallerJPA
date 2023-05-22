package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.error.IcesiException;
import co.edu.icesi.demo.mapper.IcesiUserMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import co.edu.icesi.demo.service.IcesiUserService;
import co.edu.icesi.demo.unit.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {

    private IcesiUserService icesiUserService;

    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    private IcesiRoleRepository icesiRoleRepository;

    @BeforeEach
    public void setup(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiUserMapper = spy(IcesiUserMapper.class);
        icesiUserService = new IcesiUserService(icesiUserRepository, icesiUserMapper, icesiRoleRepository);
    }

    @Test
    public void testSaveUser(){

        /*when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.ofNullable(createDefaultIcesiUser()));

        icesiUserService.save(createDefaultIcesiUserDto());

        verify(icesiRoleRepository, times(1)).findByName(any());
        verify(icesiUserMapper, times(1)).fromIcesiUserDto(any());
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(createDefaultIcesiUser())));
        */
    }


    @Test
    public void testSaveUserEmailAlreadyExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.save(createDefaultIcesiUserDto());
            fail();
        }catch (IcesiException exception){
            String message= exception.getMessage();
            System.out.println(message);
            assertEquals("User with this e-mail already exists",message);
        }
    }

    @Test
    public void testSaveUserPhoneNumberAlreadyExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));

        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.save(createDefaultIcesiUserDto());
            fail();
        }catch (IcesiException exception){
            String message= exception.getMessage();
            assertEquals("User with this phone already exists",message);
        }
    }

    @Test
    public void testSaveUserEmailAndPhoneNumberAlreadyExist(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.save(createDefaultIcesiUserDto());
            fail();
        }catch (IcesiException exception){
            String message= exception.getMessage();
            assertEquals("User already has both email and phone",message);
        }
    }

    @Test
    public void testSaveUserWithNullRole(){
        IcesiUserDto userCreateDTO = IcesiUserDto.builder().userId(UUID.randomUUID())
                .firstname("John")
                .lastName("Doe")
                .email("example@example.com")
                .password("123456")
                .phoneNumber("1234567890")
                .build();
        try{
            icesiUserService.save(userCreateDTO);
            fail();
        }catch (IcesiException exception){
            String message= exception.getMessage();
            assertEquals("User must have a role",message);
        }
    }


    @Test
    public void testSaveUserRoleNotExists(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.empty());
        try{
            icesiUserService.save(createDefaultIcesiUserDto());
            fail();
        }catch (IcesiException exception){
            String message= exception.getMessage();
            assertEquals("Role does not exist",message);
        }
    }

    /*
    @Test
    public void testExistingEmail(){

        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.saveUser(createDefaultIcesiUserDto());
            fail();
        }catch (RuntimeException exception){
            String msg = exception.getMessage();
            assertEquals("Email has already been taken", msg);
        }
    }

    @Test
    public void testExistingPhone(){

        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.saveUser(createDefaultIcesiUserDto());
            fail();
        }catch (RuntimeException exception){
            String msg = exception.getMessage();
            assertEquals("Phone number has already been taken", msg);
        }
    }

    @Test
    public void testSaveUserEmailAndPhoneNumberAlreadyExist(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultCreateRole()));
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.saveUser(createDefaultIcesiUserDto());
            fail();
        }catch (RuntimeException exception){
            String message= exception.getMessage();
            assertEquals("Both email and phone number are already taken",message);
        }
    }
    */




    private IcesiRoleDto defaultCreateRoleDTO(){
        return IcesiRoleDto.builder()
                .roleId(UUID.randomUUID())
                .name("ROLE_USER")
                .build();
    }

    private IcesiRole defaultCreateRole(){
        return IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .name("ROLE_USER")
                .build();
    }

    private IcesiUser createDefaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Juan")
                .lastName("Blanco")
                .email("cryinginside@gmail.com")
                .password("1234568")
                .phoneNumber("3173245376")
                .role(defaultCreateRole())
                .build();
    }

    private IcesiUserDto createDefaultIcesiUserDto(){
        return IcesiUserDto.builder()
                .userId(UUID.randomUUID())
                .firstname("Juan")
                .lastName("Blanco")
                .email("cryinginside@gmail.com")
                .password("1234568")
                .phoneNumber("3173245376")
                .role(defaultCreateRoleDTO())
                .build();
    }
}
