package co.com.icesi.TallerJPA.unit.service;

import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.mapper.UserMapper;
import co.com.icesi.TallerJPA.mapper.UserMapperImpl;
import co.com.icesi.TallerJPA.model.IcesiUser;
import co.com.icesi.TallerJPA.repository.UserRepository;
import co.com.icesi.TallerJPA.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;
    @BeforeEach
    private void init(){
        userRepository = mock(UserRepository.class);
        userMapper = spy(UserMapperImpl.class);
        userService = new UserService(userRepository, userMapper);
    }

    @Test
    public void testCreateUser() {
//        userService.save(defaultUserCreateDTO());
//        verify(userRepository, times(2)).save(any());
    }

    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("1234")
                .password("1234")
                .build();
    }


}
