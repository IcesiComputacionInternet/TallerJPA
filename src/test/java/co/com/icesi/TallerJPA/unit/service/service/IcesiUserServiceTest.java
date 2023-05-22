package co.com.icesi.TallerJPA.unit.service.service;

import co.com.icesi.TallerJPA.config.PasswordEncoderConfiguration;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapper;
import co.com.icesi.TallerJPA.mapper.IcesiUserMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiRole;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.IcesiRoleRepository;
import co.com.icesi.TallerJPA.repository.IcesiUserRepository;
import co.com.icesi.TallerJPA.service.IcesiUserService;
import co.com.icesi.TallerJPA.unit.service.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {
    private IcesiUserService userService;
    private IcesiUserRepository userRepository;
    private IcesiUserMapper userMapper;
    private IcesiRoleRepository roleRepository;

    @Autowired
    private PasswordEncoderConfiguration passwordEncoderConfiguration;

    @BeforeEach
    public void init(){
        userRepository = mock(IcesiUserRepository.class);
        roleRepository = mock(IcesiRoleRepository.class);
        userMapper = spy(IcesiUserMapperImpl.class);
        userService = new IcesiUserService(userRepository,roleRepository, userMapper,passwordEncoderConfiguration);
    }

    @Test
    public void testCreateUser(){
        when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultRole()));
        userService.save(defaultUserDTO());

        verify(roleRepository,times(1)).findByName(any());
        verify(userRepository,times(1)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
        verify(userMapper,times(1)).fromIcesiUserDTO(any());
    }

    @Test
    public void createUserWhenRoleDoesNotExist(){
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        try {
            userService.save(defaultUserDTO());
            fail();
        }catch (RuntimeException e){
            String exceptionMessage = e.getMessage();
            assertEquals("This role doesn't exist in the database: "+defaultUserDTO().getRole(),exceptionMessage);
        }
    }

    @Test
    public void testCreateUserWithAnEmailAlreadyInUse(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        try{
            userService.save(defaultUserDTO());
            fail();
        }catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("This user email "+ defaultUserDTO().getEmail()+" already exists in the database", message);

        }
    }

    @Test
    public void testCreateUserWithAPhoneNumberAlreadyInUse(){
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        try{
            userService.save(defaultUserDTO());
            fail();
        }catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("This user phone number "+ defaultUserDTO().getPhoneNumber()+" already exists in the database", message);

        }
    }

    @Test
    public void testCreateUserWithAnEmailAndAPhoneNumberAlreadyInUse(){
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        when(userRepository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(defaultIcesiUser()));
        try{
            userService.save(defaultUserDTO());
            fail();
        }catch (RuntimeException e){
            String message = e.getMessage();
            assertEquals("This user email "+defaultUserDTO().getEmail()+" and this phone number "+defaultUserDTO().getPhoneNumber()+" already exists in the database", message);

        }
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("Michael")
                .lastName("Jackson")
                .email("jekag85543@marikuza.com")
                .phoneNumber("228322647761")
                .password("pruebapassword")
                .role(defaultRole())
                .build();
    }

    private IcesiUserCreateDTO defaultUserDTO(){
        return IcesiUserCreateDTO.builder()
                .firstName("Michael")
                .lastName("Jackson")
                .email("jekag85543@marikuza.com")
                .phoneNumber("228322647761")
                .password("pruebapassword")
                .role(defaultRoleDTO())
                .build();
    }

    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .name("Administrador")
                .description("El administrador se encarga de administrar la página")
                .build();
    }

    private IcesiRoleCreateDTO defaultRoleDTO(){
        return   IcesiRoleCreateDTO.builder()
                .name("Administrador")
                .description("El administrador se encarga de administrar la página")
                .build();
    }
}
