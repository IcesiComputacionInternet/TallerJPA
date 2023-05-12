package co.com.icesi.tallerjpa.integration;

import co.com.icesi.tallerjpa.dto.LoginDTO;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class TallerJPATest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void contextLoads() {

    }

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
    public void testCreateUserEndPoint() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/users").content(
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
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
