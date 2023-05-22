package co.com.icesi.TallerJpa.unit.service;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.RoleChangeDTO;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.error.exception.IcesiException;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapper;
import co.com.icesi.TallerJpa.mapper.IcesiUserMapperImpl;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiRoleRepository;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import co.com.icesi.TallerJpa.service.IcesiUserService;
import co.com.icesi.TallerJpa.unit.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private IcesiUserService icesiUserService;
    private IcesiUserRepository icesiUserRepository;
    private IcesiRoleRepository icesiRoleRepository;
    private IcesiUserMapper icesiUserMapper;
    @BeforeEach
    public void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapperImpl.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiUserService = new IcesiUserService(icesiUserRepository, icesiRoleRepository, icesiUserMapper);
    }

    @Test
    @DisplayName("Create an icesi user Happy Path")
    public void testCreateUserHappyPath(){
        var actualUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(defaultIcesiRole()));
        icesiUserService.saveUser(defaultIcesiUserDTO(), actualUser.getIcesiRole().getName());
        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiUserRepository,times(1)).findByPhoneNumber(any());
        verify(icesiRoleRepository,times(1)).findByName(any());
        verify(icesiUserMapper,times(1)).fromUserDto(defaultIcesiUserDTO());
        verify(icesiUserMapper,times(1)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(1)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }

    @Test
    @DisplayName("Create an icesi user with email duplicated")
    public void testCreateIcesiUserWithEmailExists(){
        var actualUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.saveUser(defaultIcesiUserDTO(),actualUser.getIcesiRole().getName()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("Field duplicated", icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("field email is duplicated", icesiError.getDetails().get(0).getErrorMessage());


        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiUserRepository,times(1)).findByPhoneNumber(any());
        verify(icesiRoleRepository,times(0)).findByName(any());
        verify(icesiUserMapper,times(0)).fromUserDto(defaultIcesiUserDTO());
        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(0)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }

    @Test
    @DisplayName("Create an icesi user with phoneNumber duplicated")
    public void testCreateIcesiUserWithPhoneNumberExists(){
        var actualUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.saveUser(defaultIcesiUserDTO(),actualUser.getIcesiRole().getName()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("Field duplicated", icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("field phoneNumber is duplicated", icesiError.getDetails().get(0).getErrorMessage());


        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiUserRepository,times(1)).findByPhoneNumber(any());
        verify(icesiRoleRepository,times(0)).findByName(any());
        verify(icesiUserMapper,times(0)).fromUserDto(defaultIcesiUserDTO());
        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(0)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }

    @Test
    @DisplayName("Create an icesi user with email and phoneNumber duplicated")
    public void testCreateIcesiUserWithEmailAndPhoneNumberExists(){
        var actualUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(defaultIcesiUser()));

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.saveUser(defaultIcesiUserDTO(),actualUser.getIcesiRole().getName()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(2,icesiError.getDetails().size());
        assertEquals(400, icesiError.getStatus().value());
        assertEquals("Field duplicated", icesiException.getMessage());
        assertEquals("ERR_400", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("ERR_400", icesiError.getDetails().get(1).getErrorCode());
        assertEquals("field email is duplicated", icesiError.getDetails().get(0).getErrorMessage());
        assertEquals("field phoneNumber is duplicated", icesiError.getDetails().get(1).getErrorMessage());

        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiUserRepository,times(1)).findByPhoneNumber(any());
        verify(icesiRoleRepository,times(0)).findByName(any());
        verify(icesiUserMapper,times(0)).fromUserDto(defaultIcesiUserDTO());
        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(0)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }

    @Test
    @DisplayName("Create an icesi user when icesi role doesn't exists")
    public void testCreateIcesiUserWithIcesiRoleNotExists(){
        var actualUser = adminIcesiUser();
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.empty());
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.empty());

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.saveUser(defaultIcesiUserDTO(),actualUser.getIcesiRole().getName()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND.value(), icesiError.getStatus().value());
        assertEquals("Role: "+defaultIcesiUserDTO().getRole()+" not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiRole with name: "+defaultIcesiUserDTO().getRole()+" not found",
                icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserRepository,times(1)).findByEmail(any());
        verify(icesiUserRepository,times(1)).findByPhoneNumber(any());
        verify(icesiRoleRepository,times(1)).findByName(any());
        verify(icesiUserMapper,times(0)).fromUserDto(defaultIcesiUserDTO());
        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(0)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }

    @Test
    @DisplayName("Assign new role to icesi user Happy Path")
    public void testAssignRoleHappyPath(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(changeIcesiRole()));
        icesiUserService.assignRole(defaultRoleChangeDTO());
        verify(icesiUserRepository,times(1)).findByEmail(defaultIcesiUser().getEmail());
        verify(icesiRoleRepository,times(1)).findByName(changeIcesiRole().getName());
        verify(icesiUserMapper,times(1)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(1)).save(argThat(new IcesiUserMatcher(resultChangeRole())));
    }

    @Test
    @DisplayName("Assign new role to icesi user that not exists")
    public void testAssignRoleWhenUserNotExists(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.assignRole(defaultRoleChangeDTO()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND.value(), icesiError.getStatus().value());
        assertEquals("User not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiUser with email: "+defaultIcesiUserDTO().getEmail()+" not found",
                icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserRepository,times(1)).findByEmail(defaultIcesiUser().getEmail());
        verify(icesiRoleRepository,times(0)).findByName(changeIcesiRole().getName());
        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(0)).save(argThat(new IcesiUserMatcher(resultChangeRole())));

    }

    @Test
    @DisplayName("Assign new role to icesi user when role not exists")
    public void testAssignRoleThatNotExists(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.empty());
        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.assignRole(defaultRoleChangeDTO()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND.value(), icesiError.getStatus().value());
        assertEquals("Role: "+defaultRoleChangeDTO().getRole()+" not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiRole with name: "+defaultRoleChangeDTO().getRole()+" not found",
                icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserRepository,times(1)).findByEmail(defaultIcesiUser().getEmail());
        verify(icesiRoleRepository,times(1)).findByName(changeIcesiRole().getName());
        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(0)).save(argThat(new IcesiUserMatcher(resultChangeRole())));
    }

    @Test
    @DisplayName("Get an icesi user by userId Happy Path")
    public void testGetIcesiUserByUserIdHappyPath(){
        when(icesiUserRepository.findById(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiUserService.getUserById(defaultUUID());
        verify(icesiUserMapper,times(1)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(1)).findById(defaultUUID());
    }

    @Test
    @DisplayName("Get an icesi user by userId when not exists")
    public void testGetIcesiUserByUserIdWhenNotExists(){
        when(icesiUserRepository.findById(any())).thenReturn(Optional.empty());

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.getUserById(defaultUUID()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND.value(), icesiError.getStatus().value());
        assertEquals("User not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiUser with userId: "+defaultUUID()+" not found",
                icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(1)).findById(defaultUUID());
    }

    @Test
    @DisplayName("Get an icesi user by email Happy Path")
    public void testGetIcesiUserByEmailHappyPath(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(defaultIcesiUser()));
        icesiUserService.getUserByEmail(defaultIcesiUser().getEmail());
        verify(icesiUserMapper,times(1)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(1)).findByEmail(defaultIcesiUser().getEmail());
    }

    @Test
    @DisplayName("Get an icesi user by email when not exists")
    public void testGetIcesiUserByEmailWhenNotExists(){
        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.empty());

        IcesiException icesiException = assertThrows(IcesiException.class,
                () -> icesiUserService.getUserByEmail(defaultIcesiUser().getEmail()));
        IcesiError icesiError = icesiException.getError();

        assertEquals(1,icesiError.getDetails().size());
        assertEquals(HttpStatus.NOT_FOUND.value(), icesiError.getStatus().value());
        assertEquals("User not found", icesiException.getMessage());
        assertEquals("ERR_404", icesiError.getDetails().get(0).getErrorCode());
        assertEquals("IcesiUser with email: "+defaultIcesiUser().getEmail()+" not found",
                icesiError.getDetails().get(0).getErrorMessage());

        verify(icesiUserMapper,times(0)).fromIcesiUserToResponse(any());
        verify(icesiUserRepository,times(1)).findByEmail(defaultIcesiUser().getEmail());
    }

    private IcesiUser adminIcesiUser(){
        return IcesiUser.builder()
                .userId(defaultUUID())
                .firstName("John")
                .lastName("Doe")
                .email("jd.trujillo@hotmail.com")
                .phoneNumber("3147778899")
                .password("password")
                .icesiRole(adminIcesiRole())
                .build();
    }
    private IcesiRole adminIcesiRole(){
        return IcesiRole.builder()
                .name("ADMIN")
                .description("Administrador").build();
    }
    private IcesiUserRequestDTO defaultIcesiUserDTO(){
        return IcesiUserRequestDTO.builder()
                .firstName("Mariana")
                .lastName("Trujillo")
                .email("mariana.trujillo@hotmail.com")
                .phoneNumber("3147778888")
                .password("password")
                .role("ICESI_STUDENT")
                .build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(defaultUUID())
                .firstName("Mariana")
                .lastName("Trujillo")
                .email("mariana.trujillo@hotmail.com")
                .phoneNumber("3147778888")
                .password("password")
                .icesiRole(defaultIcesiRole())
                .build();
    }
    private IcesiRole defaultIcesiRole(){
        return IcesiRole.builder()
                .name("ICESI_STUDENT")
                .description("Icesi student")
                .build();
    }
    private IcesiUser resultChangeRole(){
        return IcesiUser.builder()
                .userId(defaultUUID())
                .firstName("Mariana")
                .lastName("Trujillo")
                .email("mariana.trujillo@hotmail.com")
                .phoneNumber("3147778888")
                .password("password")
                .icesiRole(changeIcesiRole())
                .build();
    }
    private IcesiRole changeIcesiRole(){
        return IcesiRole.builder()
                .name("ICESI_PROFESSOR")
                .description("Icesi professor")
                .build();
    }
    private RoleChangeDTO defaultRoleChangeDTO(){
        return RoleChangeDTO.builder()
                .email("mariana.trujillo@hotmail.com")
                .role("ICESI_PROFESSOR")
                .build();
    }
    private UUID defaultUUID(){
        return UUID.fromString("6bf95d02-1092-463d-b285-6ed84800a6e0");
    }
}
