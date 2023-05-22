package co.com.icesi.TallerJPA.integration;


import co.com.icesi.TallerJPA.config.IcesiAuthenticationManager;
import co.com.icesi.TallerJPA.config.PasswordEncoderConfiguration;
import co.com.icesi.TallerJPA.config.SecurityConfiguration;
import co.com.icesi.TallerJPA.controller.IcesiUserController;
import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.dto.LoginDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@AutoConfigureMockMvc
@Import(TestConfigurationData.class )
@ActiveProfiles(profiles = "test")
@SpringBootTest
public class IcesiUserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    private String token = "";


    @Test
    public void testCreateUserAdmin() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post("/user").content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserBank() throws Exception {
        setToken("bank@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post("/user").content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserNormal() throws Exception {
        setToken("user@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post("/user").content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    public void  setToken(String email) throws Exception {
        var login =   mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").content(
                                objectMapper.writeValueAsString(LoginDto.builder().userName(email).password("password").build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var response = objectMapper.readTree(login.getResponse().getContentAsString());
        token = response.get("token").asText();
        System.out.println("Actual token: "+ token);
    }

    public IcesiUserDTO defaultUser(){
        return  IcesiUserDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("+573123456780")
                .password("12456789")
                .email("jhon.doe@gmailTest.com")
                .role(IcesiRoleDTO.builder()
                        .name("NORMAL")
                        .build())
                .build();
    }
}
