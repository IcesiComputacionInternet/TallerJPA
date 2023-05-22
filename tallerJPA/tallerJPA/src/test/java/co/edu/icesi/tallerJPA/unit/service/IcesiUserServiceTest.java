package co.edu.icesi.tallerJPA.unit.service;

import co.edu.icesi.tallerJPA.dto.IcesiUserDTO;
import co.edu.icesi.tallerJPA.exception.ExistingException;
import co.edu.icesi.tallerJPA.mapper.IcesiUserMapper;
import co.edu.icesi.tallerJPA.model.IcesiUser;
import co.edu.icesi.tallerJPA.model.Role;
import co.edu.icesi.tallerJPA.repository.IcesiUserRepository;
import co.edu.icesi.tallerJPA.repository.RoleRepository;
import co.edu.icesi.tallerJPA.service.IcesiUserService;
import co.edu.icesi.tallerJPA.unit.matcher.IcesiUserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {
    private IcesiUserService icesiUserService;
    private IcesiUserRepository icesiUserRepository;
    private IcesiUserMapper icesiUserMapper;

    private RoleRepository roleRepository;

    @BeforeEach
    public void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapper.class);
        icesiUserService = new IcesiUserService(icesiUserRepository, roleRepository, icesiUserMapper );
    }

    @Test
    public void testSaveIcesiUser(){
        icesiUserService.save(defaultIcesiUserDTO());
        verify(icesiUserMapper, times(1)).fromIcesiUserDTO(any());
        verify(icesiUserRepository, times(1)).save(argThat(new IcesiUserMatcher(defaultIcesiUser())));
    }
    @Test
    public void testSaveUserWhenEmailExisting() {
        try {
            icesiUserService.save(defaultIcesiUserDTO());
        } catch (ExistingException e) {
            assertEquals("Email  is in use", e.getMessage());

        }
    }

    @Test
    public void testSaveUserWhenPhoneNumberExisting(){
        try{
            icesiUserService.save(defaultIcesiUserDTO());
        }catch (ExistingException e){
            assertEquals("Phone is in use", e.getMessage());
        }
    }
    @Test
    public void testSaveUserWhenEmailAndPhoneNumberExisting(){
        try{
            icesiUserService.save(defaultIcesiUserDTO());
        }catch (ExistingException e){
            assertEquals("Email and phone already exists", e.getMessage());
        }
    }

    private IcesiUserDTO defaultIcesiUserDTO(){
        return IcesiUserDTO.builder()
                .firstName("John")
                .email("jkls1998@gmail.com")
                .password("1234567")
                .lastname("Landazuri")
                .phoneNumber("3116244621")
                .role(new Role())
                .build();
    }

    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .firstName("John")
                .email("jkls1998@gmail.com")
                .password("1234567")
                .lastName("Landazuri")
                .phoneNumber("3116244621")
                .role(new Role())
                .build();
    }


}