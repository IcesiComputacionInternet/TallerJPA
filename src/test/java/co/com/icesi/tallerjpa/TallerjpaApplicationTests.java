package co.com.icesi.tallerjpa;

import co.com.icesi.tallerjpa.dto.LoginDTO;
import co.com.icesi.tallerjpa.dto.TokenDTO;
import co.com.icesi.tallerjpa.error.exception.IcesiError;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class TallerjpaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testTokenEndPoint() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //System.out.println(result.getResponse().getContentAsString());
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
        assertNotNull(token);
    }

    /*@Test
    public void testTokenEndpointWithInvalidEmail() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/token").content(
                    objectMapper.writeValueAsString(new LoginDTO("incorret@gmail.com", "password"))
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        IcesiError token = objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(token);
    }*/

}

