package co.com.icesi.demojpa.unit.configuration;

import co.com.icesi.demojpa.dto.request.LoginDTO;
import co.com.icesi.demojpa.dto.request.UserCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static String token = "";

    @Test
    void contextLoads() {
    }



    @Test
    public void testCreateUserEndPointWhenUserIsNotAuth() throws Exception {
        var result = mockMvc.perform(post("/user").content(
                        objectMapper.writeValueAsString(
                                UserCreateDTO.builder()
                                        .email("test@gmail.com")
                                        .phone("+573191111111")
                                        .firstName("John")
                                        .lastName("Doe")
                                        .password("password")
                                        .roleName("ADMIN")
                                        .build()
                        )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserEndPointWhenUserAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("123456789@gmail.com")
                                                .phone("+573197419033")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    public void testCreateUserEndPointWhenUserAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("123454489@gmail.com")
                                                .phone("+573197229033")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserEndPointWhenUserAuthUser() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("user@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("123454489@gmail.com")
                                                .phone("+573197229033")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testValidationEmailOrPhoneCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("")
                                                .phone("")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testValidationEmailCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("NoEmail")
                                                .phone("+573226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testValidationColombiaPhoneNumberCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("email@email.com")
                                                .phone("3226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testValidationEmailOrPhoneCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("")
                                                .phone("")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testValidationEmailCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("NoEmail")
                                                .phone("+573226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testValidationColombiaPhoneNumberCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        UserCreateDTO.builder()
                                                .email("email@email.com")
                                                .phone("3226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .roleName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
