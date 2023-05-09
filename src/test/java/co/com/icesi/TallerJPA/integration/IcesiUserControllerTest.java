package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.LoginDTO;
import co.com.icesi.TallerJPA.dto.TokenDTO;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
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

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class IcesiUserControllerTest {
    @Autowired
    private MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testTokenEndpoint() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
        assertNotNull(token);
        //System.out.println(result.getResponse().getContentAsString());
    }

    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("+573154620529")
                .password("1234")
                .role("ADMIN")
                .build();
    }
}
