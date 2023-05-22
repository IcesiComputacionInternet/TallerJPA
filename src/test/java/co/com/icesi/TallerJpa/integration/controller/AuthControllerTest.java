package co.com.icesi.TallerJpa.integration.controller;

import co.com.icesi.TallerJpa.dto.LoginDTO;
import co.com.icesi.TallerJpa.dto.TokenDTO;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.integration.configuration.TestConfigurationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @DisplayName("Test to token endpoint Happy Path")
    public void testTokenEndPointHappyPath() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/token")
                        .content(mapper.writeValueAsString(new LoginDTO("johndoe.user@hotmail.com","password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO tokenDTO = mapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
        assertNotNull(tokenDTO);
    }

    @Test
    @DisplayName("Test to token endpoint when bad credentials")
    public void testTokenEndPointBadCredentials() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/token").content(
                                mapper.writeValueAsString(new LoginDTO("johndoe@hotmail.com","password"))
                        ).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertNotNull(icesiError);
    }
}
