package icesi.university.accountSystem.unit;

import icesi.university.accountSystem.mapper.IcesiUserMapper;
import icesi.university.accountSystem.repository.IcesiRoleRepository;
import icesi.university.accountSystem.repository.IcesiUserRepository;
import icesi.university.accountSystem.services.UserService;

public class UserServiceTest {
        private UserService userService;
        private IcesiUserRepository repository;

        private IcesiUserMapper mapper;
        private IcesiRoleRepository roleRepository;

        /*
        @BeforeEach
        private void init(){
            repository = mock(IcesiUserRepository.class);
            roleRepository = mock(IcesiRoleRepository.class);
            mapper = spy(IcesiUserMapperImpl.class);
            userService = new UserService(repository,mapper);
        }


        @Test
        public void saveUser(){
            when(roleRepository.findByName(any())).thenReturn(Optional.of(createDefaultRole()));

            userService.save(createDefaultUserDTO());

            verify(repository,times(1)).save(argThat(new UserMatcher(createDefaultUser())));
            verify(mapper,times(1)).fromIcesiUserDTO(any());

        }

        @Test
        public void saveUserWithEmailAndPhoneNumberExist(){
            when(repository.findByEmail(any())).thenReturn(Optional.ofNullable(createDefaultUser()));
            when(repository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(createDefaultUser()));
            try {
                userService.save(createDefaultUserDTO());
                fail();
            }catch (Exception e){
                String exceptionMessage = e.getMessage();
                assertEquals("email and phoneNumber already in use",exceptionMessage);
                verify(repository, times(0)).save(any());
            }
        }

        @Test
        public void saveUserWithEmailExist(){
            when(repository.findByEmail(any())).thenReturn(Optional.ofNullable(createDefaultUser()));
            try {
                userService.save(createDefaultUserDTO());
                fail();
            }catch (Exception e){
                String exceptionMessage = e.getMessage();
                assertEquals("email already in use",exceptionMessage);
            }
        }

        @Test
        public void saveUserWithPhoneNumberExist(){
            when(repository.findByPhoneNumber(any())).thenReturn(Optional.ofNullable(createDefaultUser()));
            try {
                userService.save(createDefaultUserDTO());
                fail();
            }catch (Exception e){
                String exceptionMessage = e.getMessage();
                assertEquals("phoneNumber already in use",exceptionMessage);
            }
        }

        @Test
        public void saveUserWithRoleNull(){
            when(roleRepository.findByName(any())).thenReturn(Optional.empty());
            try {
                userService.save(createDefaultUserDTO());
            }catch (Exception e){
                String exceptionMessage = e.getMessage();
                assertEquals(NullPointerException.class,e.getClass());
            }
        }

        private IcesiUser createDefaultUser(){
            return IcesiUser.builder()
                    .firstName("Jhon")
                    .lastName("Doe")
                    .phoneNumber("312545454")
                    .password("12456789")
                    .email("testEmail@example.com")
                    .role(createDefaultRole())
                    .build();
        }

        private IcesiUserDTO createDefaultUserDTO(){
            return IcesiUserDTO.builder()
                    .firstName("Jhon")
                    .lastName("Doe")
                    .phoneNumber("312545454")
                    .password("12456789")
                    .email("testEmail@example.com")
                    .role(createDefaultRole())
                    .build();
        }

        private IcesiRole createDefaultRole(){
            return IcesiRole.builder()
                    .name("Role")
                    .description("this is a role")
                    .icesiUsers(new ArrayList<>())
                    .build();
        }

        private IcesiRoleDTO defaultRoleDTO(){
            return   IcesiRoleDTO.builder()
                    .name("Role")
                    .description("this is a role")
                    .build();
        }
        */

}
