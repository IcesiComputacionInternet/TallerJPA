package co.edu.icesi.tallerjpa.unit.service;

import co.edu.icesi.tallerjpa.dto.IcesiRoleCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiRoleShowForUserDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
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
import java.util.UUID;

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
    private IcesiRoleShowForUserDTO defaultIcesiRoleShowForUserDTO(){
        return IcesiRoleShowForUserDTO.builder()
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


}
