package co.edu.icesi.demo.integration;
import co.edu.icesi.demo.TestConfigurationData;
import co.edu.icesi.demo.dto.LoginDTO;
import co.edu.icesi.demo.dto.TokenDTO;
import co.edu.icesi.demo.dto.UserDTO;
import co.edu.icesi.demo.error.exception.IcesiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static co.edu.icesi.demo.api.UserAPI.BASE_USER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles="test")
@Import(TestConfigurationData.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public TokenDTO tokenEndpoint(String email, String password) throws Exception{
        var result= mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO(email,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
    }

    private TokenDTO tokenAdmin()throws Exception{
        return tokenEndpoint("julietav@example.com","julieta123");
    }

    private TokenDTO tokenUser()throws Exception{
        return tokenEndpoint("johndoe2@email.com","password");
    }

    private TokenDTO tokenBank()throws Exception{
        return tokenEndpoint("icesibank@email.com","password123");
    }



    @Order(1)
    @Test
    public void testCreateUserWithAdmin() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO userDTO =objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(userDTO);


    }

    @Order(2)
    @Test
    public void testCreateUserWhenEmailAlreadyExists() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setPhoneNumber("+573186441235");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isConflict())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("User email george.harrison1@email.com already exists",detail.getErrorMessage());


    }

    @Order(3)
    @Test
    public void testCreateUserWhenPhoneNumberAlreadyExists() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();

        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("george.harrison5@email.com");


        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isConflict())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Phone Number +573186441231 already exists",detail.getErrorMessage());


    }

    @Order(4)
    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAlreadyExists() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isConflict())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("User Email and Phone number already exists",detail.getErrorMessage());


    }

    @Order(4)
    @Test
    public void testCreateUserWhenRoleDoesNotExists() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("george.harrison5@email.com");
        defaultUser.setPhoneNumber("+573186441235");
        defaultUser.setRoleName("OTHER");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isNotFound())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("User role OTHER not found",detail.getErrorMessage());


    }

    @Order(5)
    @Test
    public void testCreateUserWithBank() throws Exception{

        TokenDTO tokenDTO=tokenBank();

        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("george.harrison4@email.com");
        defaultUser.setPhoneNumber("+573186441234");
        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO userDTO =objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(userDTO);

    }

    @Order(6)
    @Test
    public void testCreateBankUserWithAdmin() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();


        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultBankUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO userDTO =objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(userDTO);

    }

    @Order(7)
    @Test
    public void testCreateBankUserWithBankUser() throws Exception{

        TokenDTO tokenDTO=tokenBank();

        UserDTO defaultBank=defaultBankUserCreateDTO();
        defaultBank.setEmail("george.harrison5@email.com");
        defaultBank.setPhoneNumber("+573186441235");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultBank)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO userDTO =objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(userDTO);

    }

    @Order(8)
    @Test
    public void testCreateAdminUserWithAdmin() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultAdminUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        UserDTO userDTO =objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        assertNotNull(userDTO);


    }

    @Order(9)
    @Test
    public void testCreateAdminUserWithBankUser() throws Exception{

        TokenDTO tokenDTO=tokenBank();

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultAdminUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isForbidden())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Unauthorized: Bank user cannot create Admin users",detail.getErrorMessage());

    }

    @Order(10)
    @Test
    public void testCreateUserWithUser() throws Exception{

        TokenDTO tokenDTO=tokenUser();

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Order(11)
    @Test
    public void testCreateUserRequestMissingFiled() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setPassword("");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("password is missing",detail.getErrorMessage());


    }
    @Order(12)
    @Test
    public void testCreateUserRequestInvalidEmail1() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("@u.com");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("email Invalid email",detail.getErrorMessage());


    }

    @Order(13)
    @Test
    public void testCreateUserRequestInvalidEmail2() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("u.com");
        defaultUser.setPhoneNumber("");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("email Invalid email",detail.getErrorMessage());

    }

    @Order(14)
    @Test
    public void testCreateUserRequestInvalidPhoneNumber() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("");
        defaultUser.setPhoneNumber("1234567890123");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("phoneNumber Invalid phone number. Should be a Colombian phone number",detail.getErrorMessage());


    }

    @Order(15)
    @Test
    public void testCreateUserRequestMissingEmailAndPhoneNumber() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();
        UserDTO defaultUser=defaultUserCreateDTO();
        defaultUser.setEmail("");
        defaultUser.setPhoneNumber("");

        var result=mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUser)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals(" Email or phone number are needed!",detail.getErrorMessage());


    }

    private UserDTO defaultUserCreateDTO(){

        return UserDTO.builder()
                .firstName("George")
                .lastName("Harrison")
                .email("george.harrison1@email.com")
                .phoneNumber("+573186441231")
                .password("password")
                .roleName("USER")
                .build();
    }

    private UserDTO defaultAdminUserCreateDTO(){

        return UserDTO.builder()
                .firstName("George")
                .lastName("Harrison")
                .email("george.harrison2@email.com")
                .phoneNumber("+573186441232")
                .password("password")
                .roleName("ADMIN")
                .build();
    }

    private UserDTO defaultBankUserCreateDTO(){

        return UserDTO.builder()
                .firstName("George")
                .lastName("Harrison")
                .email("george.harrison3@email.com")
                .phoneNumber("+573186441233")
                .password("password")
                .roleName("BANK")
                .build();
    }



}
