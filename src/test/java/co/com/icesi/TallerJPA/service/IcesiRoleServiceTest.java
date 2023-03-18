package co.com.icesi.TallerJPA.service;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapper;
import co.com.icesi.TallerJPA.mapper.IcesiRoleMapperImpl;
import co.com.icesi.TallerJPA.matcher.IcesiRoleMatcher;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IcesiRoleServiceTest {
    private IcesiRoleService service;
    private IcesiRoleRepository repository;

    private IcesiRoleMapper mapper;

    @BeforeEach
    private void Init(){
        repository= mock(IcesiRoleRepository.class);
        mapper= spy(IcesiRoleMapperImpl.class);
        service = new IcesiRoleService(repository,mapper);
    }

    @Test
    public void testSave(){
        when(repository.findByName(any())).thenReturn(Optional.empty());
        service.save(defaultDTO());
        verify(repository, times(1)).save(argThat(new IcesiRoleMatcher(defaultRole())));
        verify(mapper, times(1)).fromRoleDTO(any());
    }

    @Test
    public void testCreateAroleWhenNameAlreadyExist(){
        when(repository.findByName(any())).thenReturn(Optional.of(defaultRole()));
       assertThrows(RuntimeException.class, () -> service.save(defaultDTO()));

    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }

    private IcesiRoleDTO defaultDTO(){
        return   IcesiRoleDTO.builder()
                .name("FirstRole")
                .description("This is a test for the role")
                .build();
    }


}
