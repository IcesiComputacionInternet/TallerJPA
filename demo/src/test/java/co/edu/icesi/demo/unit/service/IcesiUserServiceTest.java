package co.edu.icesi.demo.unit.service;

import co.edu.icesi.demo.dto.IcesiUserDto;
import co.edu.icesi.demo.mapper.IcesiRoleMapper;
import co.edu.icesi.demo.mapper.IcesiUserMapper;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.IcesiRoleRepository;
import co.edu.icesi.demo.repository.IcesiUserRepository;
import co.edu.icesi.demo.service.IcesiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {

    private IcesiUserService icesiUserService;

    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    @BeforeEach
    public void setup(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapper.class);
        icesiUserService = new IcesiUserService(icesiUserRepository, icesiUserMapper);
    }

    private IcesiUser createDefaultIcesiUser(){
        return IcesiUser.builder()
                .firstname("Juan")
                .lastName("Blanco")
                .email("cryinginside@gmail.com")
                .password("1234568")
                .phoneNumber("3173245376")
                .build();
    }

    private IcesiUserDto createDefaultIcesiUserDto(){
        return IcesiUserDto.builder()
                .firstname("Juan")
                .lastName("Blanco")
                .email("cryinginside@gmail.com")
                .password("1234568")
                .phoneNumber("3173245376")
                .build();
    }

    @Test
    public void testSaveUser(){
        icesiUserService.saveUser(createDefaultIcesiUserDto());
        IcesiUser icesiUser = createDefaultIcesiUser();

        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(icesiUser)));
    }

    @Test
    public void testExistingEmail(){

        when(icesiUserRepository.findByEmail(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.saveUser(createDefaultIcesiUserDto());
            fail();
        }catch (RuntimeException exception){
            String msg = exception.getMessage();
            assertEquals("Email has already been taken", msg);
        }
    }

    @Test
    public void testExistingPhone(){

        when(icesiUserRepository.findByPhoneNumber(any())).thenReturn(Optional.of(createDefaultIcesiUser()));
        try{
            icesiUserService.saveUser(createDefaultIcesiUserDto());
            fail();
        }catch (RuntimeException exception){
            String msg = exception.getMessage();
            assertEquals("Phone number has already been taken", msg);
        }
    }
}
