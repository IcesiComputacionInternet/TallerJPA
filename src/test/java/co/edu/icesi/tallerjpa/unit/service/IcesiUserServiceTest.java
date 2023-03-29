package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapperImpl;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {
    private IcesiUserService icesiUserService;
    private IcesiUserRepository icesiUserRepository;
    private IcesiUserMapper icesiUserMapper;
    private IcesiRoleRepository icesiRoleRepository;

    @BeforeEach
    private void init(){
      icesiUserRepository = mock(IcesiUserRepository.class);
      icesiRoleRepository = mock(IcesiRoleRepository.class);
      icesiUserMapper = spy(IcesiUserMapperImpl.class);
      icesiUserService = new IcesiUserService(icesiUserRepository, icesiRoleRepository, icesiUserMapper);
    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name("Admin")
                .build();

    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name("Admin")
                .build();

    }
    private IcesiUserCreateDTO defaultIcesiUserCreateDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRoleCreateDTO(defaultIcesiRoleCreateDTO())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(null)
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .icesiAccounts(null)
                .build();
    }

    @Test
    public void testCreateIcesiUser(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.save(defaultIcesiUserCreateDTO());
        IcesiUser icesiUser1 = IcesiUser.builder()
                .userId(null)
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .build();
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser1)));
    }

    @Test
    public void testCreateUserWhenEmailAlreadyExists() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        Exception exception = assertThrows(RuntimeException.class, () -> icesiUserService.save(icesiUserCreateDTO));
        assertEquals("There is already a user with the email " + defaultIcesiUserCreateDTO().getEmail() + "\n", exception.getMessage());
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        Exception exception = assertThrows(RuntimeException.class, () -> icesiUserService.save(icesiUserCreateDTO));
        assertEquals("There is already a user with the phone number " + defaultIcesiUserCreateDTO().getPhoneNumber() + "\n", exception.getMessage());
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        Exception exception = assertThrows(RuntimeException.class, () -> icesiUserService.save(icesiUserCreateDTO));
        String exceptionMessage = "There is already a user with the email " + defaultIcesiUserCreateDTO().getEmail() + "\n";
        exceptionMessage += "There is already a user with the phone number " + defaultIcesiUserCreateDTO().getPhoneNumber() + "\n";
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void testCreateIcesiUserWithNotExistingIcesiRole(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.ofNullable((null)));
        Exception exception = assertThrows(RuntimeException.class, () -> icesiUserService.save(defaultIcesiUserCreateDTO()));
        assertEquals("There is no role with that name", exception.getMessage());
    }

    @Test
    public void testGenerateUUID(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.save(defaultIcesiUserCreateDTO());
        verify(icesiUserRepository, times(1)).save(argThat(x -> x.getUserId() != null));
    }
}
