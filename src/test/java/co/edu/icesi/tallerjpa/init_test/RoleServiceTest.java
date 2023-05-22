package co.edu.icesi.tallerjpa.init_test;

import co.edu.icesi.tallerjpa.dto.IcesiRoleDTO;
import co.edu.icesi.tallerjpa.exception.DataAlreadyExistException;
import co.edu.icesi.tallerjpa.exception.InvalidDataException;
import co.edu.icesi.tallerjpa.mapper.IcesiRoleMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.service.IcesiRoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

public class RoleServiceTest {
    @Mock
    private IcesiRoleRepository icesiRoleRepository;

    @Mock
    private IcesiRoleMapper icesiRoleMapper;

    @InjectMocks
    private IcesiRoleService icesiRoleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRole() throws InvalidDataException, DataAlreadyExistException {
        // Arrange
        IcesiRoleDTO roleDTO = new IcesiRoleDTO();
        roleDTO.setName("Admin");

        when(icesiRoleRepository.findByName(roleDTO.getName())).thenReturn(Optional.empty());
        when(icesiRoleMapper.toIcesiRole(roleDTO)).thenReturn(new IcesiRole());
        //when(icesiRoleRepository.save(any(IcesiRole.class))).thenReturn(new IcesiRole());

        // Act
        IcesiRole result = icesiRoleService.createRole(roleDTO);

        // Assert
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testCreateRoleWithEmptyName() {
        // Arrange
        IcesiRoleDTO roleDTO = new IcesiRoleDTO();
        roleDTO.setName("");

        // Act and Assert
        Assertions.assertThrows(InvalidDataException.class, () -> icesiRoleService.createRole(roleDTO));
    }

    @Test
    void testCreateRoleWithNameAlreadyExists() {
        // Arrange
        IcesiRoleDTO roleDTO = new IcesiRoleDTO();
        roleDTO.setName("Admin");

        when(icesiRoleRepository.findByName(roleDTO.getName())).thenReturn(Optional.of(new IcesiRole()));

        // Act and Assert
        Assertions.assertThrows(DataAlreadyExistException.class, () -> icesiRoleService.createRole(roleDTO));
    }

}
