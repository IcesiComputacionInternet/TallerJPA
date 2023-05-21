package co.com.icesi.tallerjpa.integration.controller;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.integration.configuration.TestConfigurationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Value("${security.token.permanent}")
    private String key;

    @Test
    public void testCreateUserEndPointWhenUserIsNotAuth() throws Exception {
        var result = mvc.perform(post("/users").content(
                        mapper.writeValueAsString(
                                RequestUserDTO.builder()
                                        .email("123456789@gmail.com")
                                        .phoneNumber("+573197419033")
                                        .firstName("John")
                                        .lastName("Doe")
                                        .password("password")
                                        .role("ADMIN")
                                        .build()
                        )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserEndPointWhenUserIsAuth() throws Exception {
        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(
                                RequestUserDTO.builder()
                                        .email("12345678910@gmail.com")
                                        .phoneNumber("+573197419034")
                                        .firstName("John")
                                        .lastName("Doe")
                                        .password("password")
                                        .role("ADMIN")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
