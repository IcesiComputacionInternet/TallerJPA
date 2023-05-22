package icesi.university.accountSystem.unit;

import icesi.university.accountSystem.dto.RequestUserDTO;
import icesi.university.accountSystem.exception.ExistsException;
import icesi.university.accountSystem.mapper.IcesiUserMapper;
import icesi.university.accountSystem.model.IcesiRole;
import icesi.university.accountSystem.model.IcesiUser;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import icesi.university.accountSystem.services.UserService;
import icesi.university.accountSystem.unit.matcher.UserMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
        private UserService userService;
        private IcesiUserRepository userRepository;

        private IcesiUserMapper userMapper;
        private IcesiRoleRepository roleRepository;

        @BeforeEach
        public void init() {
                userRepository = mock(IcesiUserRepository.class);
                roleRepository = mock(IcesiRoleRepository.class);
                userMapper = spy(IcesiUserMapper.class);

                userService = new UserService(userRepository, userMapper, roleRepository);
        }

        @Test
        public void testCreateUser(){
                when(roleRepository.findByName(any())).thenReturn(Optional.ofNullable(defaultRole()));

                userService.save(defaultUserDTO());
                verify(roleRepository, times(1)).findByName(any());
                verify(userMapper, times(1)).fromIcesiUserDTO(any());
                verify(userRepository, times(1)).save(argThat(new UserMatcher(defaultIcesiUser())));
        }
        @Test
        public void testCreateUserWhenEmailAlreadyExist(){
                when(userRepository.existsByEmail(any())).thenReturn(true);

                try{
                        userService.save(defaultUserDTO());
                }catch (ExistsException e){
                        assertEquals("Email already exists", e.getMessage());
                }

        }

        @Test
        public void testCreateUserWhenPhoneNumberAlreadyExist(){
                when(userRepository.existsByPhoneNumber(any())).thenReturn(true);

                try{
                        userService.save(defaultUserDTO());
                }catch (ExistsException e){
                        assertEquals("Phone number already exists", e.getMessage());
                }

        }

        @Test
        public void testCreateUserWhenEmailAndPhoneNumberAlreadyExist(){
                when(userRepository.existsByPhoneNumber(any())).thenReturn(true);
                when(userRepository.existsByEmail(any())).thenReturn(true);

                try{
                        userService.save(defaultUserDTO());
                }catch (ExistsException e){
                        assertEquals("Email and Phone is already used", e.getMessage());
                }

        }

        private RequestUserDTO defaultUserDTO(){
                return RequestUserDTO.builder()
                        .firstName("john")
                        .lastName("doe")
                        .email("johndoe@gmail.com")
                        .phoneNumber("12345")
                        .password("12345")
                        .role("ADMIN")
                        .build();
        }

        private IcesiUser defaultIcesiUser(){
                return IcesiUser.builder()
                        .firstName("juan")
                        .lastName("palta")
                        .email("juanpalta24@hotmail.com")
                        .phoneNumber("12345")
                        .password("12345")
                        .role(defaultRole())
                        .build();
        }

        private IcesiRole defaultRole(){
                return IcesiRole.builder()
                        .roleId(UUID.randomUUID())
                        .name("ADMIN")
                        .description("Admin")
                        .build();
        }

}
