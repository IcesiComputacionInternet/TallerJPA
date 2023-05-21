package co.com.icesi.tallerjpa.integration.controller;

import co.com.icesi.tallerjpa.dto.LoginDTO;
import co.com.icesi.tallerjpa.integration.configuration.TestConfigurationData;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testLoginEndPoint() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/login").content(
                        mapper.writeValueAsString(
                                new LoginDTO("admin@email.com", "password")
                        )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testLoginEndPointWhenBadCredentials() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/login").content(
                        mapper.writeValueAsString(
                                new LoginDTO("admin@email.com", "123456")
                        )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
