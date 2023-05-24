package co.com.icesi.demojpa.integration;

import co.com.icesi.demojpa.TestConfigurationData;
import co.com.icesi.demojpa.dto.*;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.error.exception.IcesiError;
import co.com.icesi.demojpa.error.exception.IcesiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class RoleTest{

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Test
    public void testRoleEndpointHappyPath() throws Exception {
        token =mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles/").content(
                                objectMapper.writeValueAsString(new RoleCreateDTO(
                                        "rol de prueba",
                                        "TEST"
                                )))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk());
        RoleCreateDTO roleCreateDTO = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), RoleCreateDTO.class);
        assertNotNull(roleCreateDTO);
        assertEquals("rol de prueba", roleCreateDTO.getDescription());
    }
    @Test
    public void testRoleEndpointNoAuth() throws Exception {
        token =mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles/").content(
                                objectMapper.writeValueAsString(new RoleCreateDTO(
                                        "rol de prueba",
                                        "TEST"
                                )))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testRoleEndpointUsedName() throws Exception {
        token =mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/roles/").content(
                                objectMapper.writeValueAsString(new RoleCreateDTO(
                                        "rol de prueba",
                                        "ADMIN"
                                )))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().is5xxServerError());
        IcesiError icesiError = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
        assertEquals("Ya existe un rol con este nombre", icesiError.getDetails());
    }
}

