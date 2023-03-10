package co.edu.icesi.tallerjpa.runableartefact.unit.service;


import co.edu.icesi.tallerjpa.runableartefact.dto.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiUserMapper;
import co.edu.icesi.tallerjpa.runableartefact.mapper.IcesiUserMapperImpl;
import co.edu.icesi.tallerjpa.runableartefact.model.IcesiUser;
import co.edu.icesi.tallerjpa.runableartefact.repository.IcesiUserRepository;
import co.edu.icesi.tallerjpa.runableartefact.service.IcesiUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class IcesiUserServiceTest {

    private IcesiUserService icesiUserService;

    private IcesiUserRepository icesiUserRepository;

    private IcesiUserMapper icesiUserMapper;

    @BeforeEach
    private void init(){
        icesiUserRepository = mock(IcesiUserRepository.class);
        icesiUserMapper = spy(IcesiUserMapperImpl.class);
        //icesiUserService = new IcesiUserService(icesiUserRepository, icesiUserMapper);
    }

    @Test
    public void saveNewUserTest(){
        //when(icesiUserRepository.save(any())).thenReturn();
        icesiUserService.saveNewUser(createDefaultIcesiUser());
        IcesiUser icesiUser = IcesiUser.builder()
                .firstName("Camilo")
                .lastName("Campaz").build();
        //verify(,times(1)).setEmail(any());
        verify(icesiUserRepository, times(1)).save(any());
    }

    private IcesiUserDTO createDefaultIcesiUser(){
        return IcesiUserDTO.builder()
                .firstName("Camilo")
                .lastName("Campaz")
                .email("camilocampazj@gmail.com")
                .password("12345")
                .build();
    }
}
