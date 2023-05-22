package co.edu.icesi.demo.integration;
import co.edu.icesi.demo.TestConfigurationData;
import co.edu.icesi.demo.dto.LoginDTO;
import co.edu.icesi.demo.dto.TokenDTO;
import co.edu.icesi.demo.dto.UserCreateDTO;
import co.edu.icesi.demo.error.exception.IcesiError;
import co.edu.icesi.demo.error.exception.IcesiException;
import co.edu.icesi.demo.model.IcesiUser;
import co.edu.icesi.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
        UserCreateDTO userCreateDTO =objectMapper.readValue(result.getResponse().getContentAsString(),UserCreateDTO.class);
        assertNotNull(userCreateDTO);


    }

    @Test
    public void testCreateUserWithBank() throws Exception{

        TokenDTO tokenDTO=tokenBank();

        UserCreateDTO defaultUser=defaultUserCreateDTO();
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
        UserCreateDTO userCreateDTO =objectMapper.readValue(result.getResponse().getContentAsString(),UserCreateDTO.class);
        assertNotNull(userCreateDTO);

    }

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
        UserCreateDTO userCreateDTO =objectMapper.readValue(result.getResponse().getContentAsString(),UserCreateDTO.class);
        assertNotNull(userCreateDTO);

    }

    @Test
    public void testCreateBankUserWithBankUser() throws Exception{

        TokenDTO tokenDTO=tokenBank();

        UserCreateDTO defaultBank=defaultBankUserCreateDTO();
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
        UserCreateDTO userCreateDTO =objectMapper.readValue(result.getResponse().getContentAsString(),UserCreateDTO.class);
        assertNotNull(userCreateDTO);

    }

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
        UserCreateDTO userCreateDTO =objectMapper.readValue(result.getResponse().getContentAsString(),UserCreateDTO.class);
        assertNotNull(userCreateDTO);


    }

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

    private UserCreateDTO defaultUserCreateDTO(){

        return UserCreateDTO.builder()
                .firstName("George")
                .lastName("Harrison")
                .email("george.harrison1@email.com")
                .phoneNumber("+573186441231")
                .password("password")
                .roleName("USER")
                .build();
    }

    private UserCreateDTO defaultAdminUserCreateDTO(){

        return UserCreateDTO.builder()
                .firstName("George")
                .lastName("Harrison")
                .email("george.harrison2@email.com")
                .phoneNumber("+573186441232")
                .password("password")
                .roleName("ADMIN")
                .build();
    }

    private UserCreateDTO defaultBankUserCreateDTO(){

        return UserCreateDTO.builder()
                .firstName("George")
                .lastName("Harrison")
                .email("george.harrison3@email.com")
                .phoneNumber("+573186441233")
                .password("password")
                .roleName("BANK")
                .build();
    }



}
