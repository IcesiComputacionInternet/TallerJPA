package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.TestConfigurationData;
import co.edu.icesi.tallerjpa.config.PasswordEncoderConfiguration;
import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.enums.NameIcesiRole;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiException;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapperImpl;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class IcesiUserServiceTest {
    private IcesiUserService icesiUserService;
    private IcesiUserRepository icesiUserRepository;
    private IcesiUserMapper icesiUserMapper;
    private IcesiRoleRepository icesiRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void init(){
      icesiUserRepository = mock(IcesiUserRepository.class);
      icesiRoleRepository = mock(IcesiRoleRepository.class);
      icesiUserMapper = spy(IcesiUserMapperImpl.class);

      icesiUserService = new IcesiUserService(icesiUserRepository, icesiRoleRepository, icesiUserMapper, passwordEncoder);
    }

    private IcesiRoleCreateDTO defaultIcesiRoleCreateDTO(){
        return IcesiRoleCreateDTO.builder()
                .description("Manage the system")
                .name(NameIcesiRole.USER.toString())
                .build();

    }

    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name(NameIcesiRole.USER.toString())
                .build();

    }

    private IcesiRole adminIcesiRole(){
        return IcesiRole.builder()
                .description("Manage the system")
                .name(NameIcesiRole.ADMIN.toString())
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
                .userId(UUID.fromString("c75bf838-8f38-403d-b64f-cca8b7a181d8"))
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .icesiAccounts(null)
                .build();
    }

    private IcesiUser adminIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("7e39d68f-dc03-4634-92d1-37bb4b1865e3"))
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepitoperez@gmail.com")
                .phoneNumber("3125551223")
                .password("password")
                .icesiRole(adminIcesiRole())
                .icesiAccounts(null)
                .build();
    }

    @Test
    public void testCreateIcesiUser(){
        IcesiUser icesiUser = adminIcesiUser();
        when(icesiRoleRepository.findByName(NameIcesiRole.ADMIN.toString())).thenReturn(Optional.of(adminIcesiRole()));
        when(icesiRoleRepository.findByName(NameIcesiRole.USER.toString())).thenReturn(Optional.of(defaultIcesiRole()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        icesiUserService.save(defaultIcesiUserCreateDTO(), icesiUser.getUserId().toString());
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
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiUserService.save(icesiUserCreateDTO, defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field email or phone number: There is already a user with the email "+icesiUserCreateDTO.getEmail()+". ", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is already a user with the email " + icesiUserCreateDTO.getEmail() + ". ", exception.getMessage());
    }

    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiUserService.save(icesiUserCreateDTO, defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("field email or phone number: There is already a user with the phone number "+icesiUserCreateDTO.getPhoneNumber()+". ", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is already a user with the phone number " + icesiUserCreateDTO.getPhoneNumber() + ". ", exception.getMessage());
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists() {
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        IcesiException exception = assertThrows(IcesiException.class, () -> icesiUserService.save(icesiUserCreateDTO, defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        String exceptionMessage = "field email or phone number: There is already a user with the email "+icesiUserCreateDTO.getEmail()+". ";
        exceptionMessage += "There is already a user with the phone number "+icesiUserCreateDTO.getPhoneNumber()+". ";
        assertEquals(exceptionMessage, icesiError.getDetails().get(0).getErrorMessage());

        exceptionMessage = "There is already a user with the email " + defaultIcesiUserCreateDTO().getEmail() + ". ";
        exceptionMessage += "There is already a user with the phone number " + defaultIcesiUserCreateDTO().getPhoneNumber() + ". ";
        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    public void testCreateIcesiUserWithNotExistingIcesiRole(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.ofNullable(null));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(null));
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.ofNullable((null)));

        IcesiException exception = assertThrows(IcesiException.class, () -> icesiUserService.save(defaultIcesiUserCreateDTO(), defaultIcesiUser().getUserId().toString()));
        IcesiError icesiError = exception.getError();
        assertEquals(1, icesiError.getDetails().size());
        assertEquals(404, icesiError.getStatus().value());
        assertEquals("role with name: "+defaultIcesiUserCreateDTO().getIcesiRoleCreateDTO().getName() + " not found", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("There is no role with that name", exception.getMessage());
    }

    @Test
    public void testGenerateUUID(){
        IcesiUser icesiUser = defaultIcesiUser();
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        when(icesiUserRepository.findById(icesiUser.getUserId())).thenReturn(Optional.of(icesiUser));
        icesiUserService.save(defaultIcesiUserCreateDTO(), icesiUser.getUserId().toString());
        verify(icesiUserRepository, times(1)).save(argThat(x -> x.getUserId() != null));
    }
}
