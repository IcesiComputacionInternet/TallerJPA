package co.edu.icesi.tallerjpa.init_test;


import co.edu.icesi.tallerjpa.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.exception.DuplicateDataException;
import co.edu.icesi.tallerjpa.exception.MissingParameterException;
import co.edu.icesi.tallerjpa.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.model.IcesiRole;
import co.edu.icesi.tallerjpa.model.IcesiUser;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import co.edu.icesi.tallerjpa.repository.IcesiRoleRepository;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.service.IcesiRoleService;
import co.edu.icesi.tallerjpa.service.IcesiUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private IcesiUserRepository icesiUserRepository;

    @Mock
    private IcesiUserMapper icesiUserMapper;

    @Mock
    private IcesiRoleRepository icesiRoleRepository;

    @Mock
    private IcesiRoleService icesiRoleService;

    @Mock
    private IcesiAccountRepository icesiAccountRepository;

    @InjectMocks
    private IcesiUserService icesiUserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNewUser() throws DuplicateDataException, MissingParameterException {
        // Arrange
        IcesiUserDTO userDTO = new IcesiUserDTO();
        userDTO.setEmail("john@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRoleName("Admin");

        IcesiUser user = new IcesiUser();
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        when(icesiUserRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByPhoneNumber(userDTO.getPhoneNumber())).thenReturn(Optional.empty());
        when(icesiRoleRepository.findByName(userDTO.getRoleName())).thenReturn(Optional.of(new IcesiRole()));
        when(icesiUserMapper.toIcesiUser(userDTO)).thenReturn(user);
        when(icesiUserRepository.save(any(IcesiUser.class))).thenReturn(user);

        // Act
        IcesiUser result = icesiUserService.saveNewUser(userDTO);

        // Assert
        Assertions.assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    void testSaveNewUserWithMissingParameters() {
        // Arrange
        IcesiUserDTO userDTO = new IcesiUserDTO();

        // Act and Assert
        Assertions.assertThrows(MissingParameterException.class, () -> icesiUserService.saveNewUser(userDTO));
    }

    @Test
    void testSaveNewUserWithDuplicateEmail() {
        // Arrange
        IcesiUserDTO userDTO = new IcesiUserDTO();
        userDTO.setEmail("john@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRoleName("Admin");

        when(icesiUserRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new IcesiUser()));

        // Act and Assert
        Assertions.assertThrows(DuplicateDataException.class, () -> icesiUserService.saveNewUser(userDTO));
    }

    @Test
    void testSaveNewUserWithDuplicatePhoneNumber() {
        // Arrange
        IcesiUserDTO userDTO = new IcesiUserDTO();
        userDTO.setEmail("john@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRoleName("Admin");

        when(icesiUserRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByPhoneNumber(userDTO.getPhoneNumber())).thenReturn(Optional.of(new IcesiUser()));

        // Act and Assert
        Assertions.assertThrows(DuplicateDataException.class, () -> icesiUserService.saveNewUser(userDTO));
    }

    @Test
    void testSaveNewUserWithMissingRole() {
        // Arrange
        IcesiUserDTO userDTO = new IcesiUserDTO();
        userDTO.setEmail("john@example.com");
        userDTO.setPhoneNumber("1234567890");
        userDTO.setRoleName("Admin");

        when(icesiUserRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(icesiUserRepository.findByPhoneNumber(userDTO.getPhoneNumber())).thenReturn(Optional.empty());
        when(icesiRoleRepository.findByName(userDTO.getRoleName())).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(MissingParameterException.class, () -> icesiUserService.saveNewUser(userDTO));
    }
}

