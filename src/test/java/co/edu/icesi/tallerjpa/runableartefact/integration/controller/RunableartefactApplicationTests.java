package co.edu.icesi.tallerjpa.runableartefact.integration.controller;

import co.edu.icesi.tallerjpa.runableartefact.dto.LoginDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
class RunableartefactApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    void tokenCreation() throws Exception {
        token = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                        objectMapper.writeValueAsString(new LoginDTO("test@email.com", "password"))
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
    }

    void unauthorizedTokenCreation() throws Exception{
        token = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                        objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
    }
    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testTokenEndpoint() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("test@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = result.getResponse().getContentAsString();
        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTokenEndpointWithInvalidCredentials() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("invalid", "invalid"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUser() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("holi@email.com")
                                                .password("password")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("ADMIN")
                                                .phoneNumber("3176162821")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUserWithInvalidEmail() throws Exception {
        unauthorizedTokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("invalid")
                                                .password("password")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("USER")
                                                .phoneNumber("3176162821")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }
}
