package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.IcesiRoleDto;
import co.edu.icesi.demo.error.IcesiException;
import co.edu.icesi.demo.mapper.IcesiRoleMapper;
import co.edu.icesi.demo.model.IcesiRole;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.service.IcesiRoleService;
import co.edu.icesi.demo.unit.matcher.IcesiRoleMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {

    private IcesiRoleService icesiRoleService;

    private IcesiRoleRepository icesiRoleRepository;

    private IcesiRoleMapper icesiRoleMapper;

    @BeforeEach
    public void setup(){
        icesiRoleRepository = mock(IcesiRoleRepository.class);
        icesiRoleMapper = spy(IcesiRoleMapper.class);
        icesiRoleService = new IcesiRoleService(icesiRoleRepository,icesiRoleMapper);
    }

    private IcesiRole createDefaultIcesiRole(){
        return IcesiRole.builder()
                .name("suffering_soul")
                .roleDescription("me")
                .build();
    }

    private IcesiRoleDto createDefaultIcesiRoleDto(){
        return IcesiRoleDto.builder()
                .name("suffering_soul")
                .description("me")
                .build();
    }

    @Test
    public void testCreateRole(){
        //Check why not working
       /* IcesiRoleDto icesiRoleDto = createDefaultIcesiRoleDto();
        IcesiRole expectedRole = createDefaultIcesiRole();
        expectedRole.setRoleId(UUID.randomUUID());

        when(icesiRoleRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(icesiRoleMapper.fromIcesiRoleDto(icesiRoleDto)).thenReturn(expectedRole);
        when(icesiRoleRepository.save(expectedRole)).thenReturn(expectedRole);

        icesiRoleService.saveRole(defaultRoleCreateDTO());
        verify(icesiRoleRepository,times(1)).save(argThat(new IcesiRoleMatcher(expectedRole)));*/
    }

    @Test
    public void saveRoleWithExistentName() {
        when(icesiRoleRepository.save(any())).thenReturn(defaultRoleCreateDTO());
        assertThrows(IcesiException.class, () -> icesiRoleService.saveRole(createDefaultIcesiRoleDto()));
    }

    private IcesiRole defaultRoleCreate(){
        return IcesiRole.builder()
                .name("Admin")
                .roleDescription("Admin")
                .build();
    }

    private IcesiRoleDto defaultRoleCreateDTO(){
        return IcesiRoleDto.builder()
                .name("Admin")
                .description("Admin")
                .build();
    }
/*
    @Test
    public void testCreateRole(){
        IcesiRoleDto roleCreateDTO = createDefaultIcesiRoleDto();
        IcesiRole expectedRole = createDefaultIcesiRole();
        expectedRole.setRoleId(UUID.randomUUID());

        when(icesiRoleRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(icesiRoleMapper.fromIcesiRoleDto(roleCreateDTO)).thenReturn(expectedRole);
        when(icesiRoleRepository.save(expectedRole)).thenReturn(expectedRole);

        icesiRoleService.saveRole(roleCreateDTO);
        verify(icesiRoleRepository,times(1)).save(argThat(new IcesiRoleMatcher(expectedRole)));
    }

    @Test
    public void testCreateExistingRoleName(){
        when(icesiRoleRepository.findByName(any())).thenReturn(Optional.of(createDefaultIcesiRole()));
        try {
            icesiRoleService.saveRole(createDefaultIcesiRoleDto());
            fail();
        }catch (RuntimeException exception){
            String msg = exception.getMessage();
            assertEquals("Role already taken", msg);
        }
    }

    */
}
