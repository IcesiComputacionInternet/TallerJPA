package co.com.icesi.TallerJpa.integration.controller;

import co.com.icesi.TallerJpa.dto.IcesiUserRequestDTO;
import co.com.icesi.TallerJpa.dto.TokenDTO;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.integration.configuration.TestConfigurationData;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class IcesiUserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private IcesiUserRepository userRepository;

    TokenDTO tokenDTO;

    @Test
    @DisplayName("Create an icesi user when user is not authenticated")
    public void testCreateIcesiUserWhenNotAuthenticated() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(mapper.writeValueAsString(new IcesiUserRequestDTO("Mariana","Trujillo","mariana.trujillo@email.com","3206373409","password","USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
