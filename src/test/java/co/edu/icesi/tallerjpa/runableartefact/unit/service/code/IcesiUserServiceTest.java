package co.edu.icesi.tallerjpa.runableartefact.unit.service.code;


import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.DataAlreadyExist;
import co.edu.icesi.tallerjpa.runableartefact.exception.implementation.ParameterRequired;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiUserMapperImpl;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiRole;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.runableartefact.service.AuthoritiesService;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiUserService;
import co.edu.icesi.tallerjpa.runableartefact.unit.service.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {

    private IcesiUserService icesiUserService;

    private IcesiUserRepository icesiUserRepository;

    private IcesiRoleRepository icesiRoleRepository;

    private AuthoritiesService authoritiesService;

    private IcesiUserMapper icesiUserMapper;

    @BeforeEach
    public void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        authoritiesService = mock(AuthoritiesService.class);
        icesiUserMapper = spy(IcesiUserMapperImpl.class);
        icesiUserService = new IcesiUserService(icesiUserRepository, icesiRoleRepository,authoritiesService, icesiUserMapper);
    }

    @Test
    public void saveNewUserTest(){
        when(icesiUserRepository.save(any())).thenReturn(createDefaultIcesiUser());
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.ofNullable(createDefaultRole()));
        when(icesiUserMapper.toIcesiUser(any())).thenReturn(createDefaultIcesiUser());
        when(icesiRoleRepository.existsByName(any())).thenReturn(true);
        when(icesiUserRepository.existsByEmail(any())).thenReturn(false);
        when(icesiUserRepository.existsByPhoneNumber(any())).thenReturn(false);

        icesiUserService.saveNewUser(createDefaultIcesiUserDTO());

        verify(icesiUserRepository, times(1)).save(any());
        verify(icesiRoleRepository, times(1)).findByName(any());
        verify(icesiUserMapper, times(1)).toIcesiUser(any());
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(createDefaultIcesiUser())));
    }

    @Test
    public void saveNewUserWhenEmailAlreadyExistsTest(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.ofNullable(createDefaultRole()));
        when(icesiUserMapper.toIcesiUser(any())).thenReturn(createDefaultIcesiUser());
        when(icesiUserRepository.existsByEmail(any())).thenReturn(true);

        try {
            icesiUserService.saveNewUser(createDefaultIcesiUserDTO());
        } catch (DataAlreadyExist e) {
            assertEquals("Email already exists", e.getMessage());
        }
    }

    @Test
    public void saveNewUserWhenPhoneNumberAlreadyExistsTest(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.ofNullable(createDefaultRole()));
        when(icesiUserMapper.toIcesiUser(any())).thenReturn(createDefaultIcesiUser());
        when(icesiUserRepository.existsByEmail(any())).thenReturn(false);
        when(icesiUserRepository.existsByPhoneNumber(any())).thenReturn(true);

        try {
            icesiUserService.saveNewUser(createDefaultIcesiUserDTO());
        } catch (DataAlreadyExist e) {
            assertEquals("Phone number already exists", e.getMessage());
        }
    }

    @Test
    public void saveNewUserWhenEmailAndPhoneNumberExistsTest(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.ofNullable(createDefaultRole()));
        when(icesiUserMapper.toIcesiUser(any())).thenReturn(createDefaultIcesiUser());
        when(icesiUserRepository.existsByEmail(any())).thenReturn(true);
        when(icesiUserRepository.existsByPhoneNumber(any())).thenReturn(true);

        try {
            icesiUserService.saveNewUser(createDefaultIcesiUserDTO());
        } catch (DataAlreadyExist e) {
            assertEquals("Email and phone number already exists", e.getMessage());
        }
    }

    @Test
    public void saveNewUserWhenRoleDoesNotExistTest(){
        when(icesiUserMapper.toIcesiUser(any())).thenReturn(createDefaultIcesiUser());
        when(icesiUserRepository.existsByEmail(any())).thenReturn(false);
        when(icesiUserRepository.existsByPhoneNumber(any())).thenReturn(false);

        when(icesiRoleRepository.existsByName(any())).thenReturn(false);
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.empty());

        try {
            icesiUserService.saveNewUser(createDefaultIcesiUserDTO());
        } catch (ParameterRequired e) {
            assertEquals("Role is required", e.getMessage());
        }
    }



    private IcesiUserDTO createDefaultIcesiUserDTO(){
        return IcesiUserDTO.builder()
                .firstName("Camilo")
                .lastName("Campaz")
                .email("testEmail@gmail.com")
                .phoneNumber("123456789")
                .password("12345")
                .roleName("ADMIN")
                .build();
    }

    private IcesiRole createDefaultRole(){
        return IcesiRole.builder()
                .name("ADMIN")
                .build();
    }

    private IcesiUser createDefaultIcesiUser() {

        return IcesiUser.builder()
                .userId(UUID.randomUUID())
                .firstName("Camilo")
                .lastName("Campaz")
                .email("testEmail@gmail.com")
                .phoneNumber("123456789")
                .password("12345")
                .role(createDefaultRole())
                .build();
    }
}
