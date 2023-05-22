package co.com.icesi.TallerJpa.integration.controller;

import co.com.icesi.TallerJpa.controller.api.IcesiUserApi;
import co.com.icesi.TallerJpa.dto.*;
import co.com.icesi.TallerJpa.integration.configuration.TestConfigurationData;
import co.com.icesi.TallerJpa.model.IcesiRole;
import co.com.icesi.TallerJpa.model.IcesiUser;
import co.com.icesi.TallerJpa.repository.IcesiUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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

import java.util.UUID;

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

    @Test
    @DisplayName("Create an icesi user with admin credentials")
    public void testCreateIcesiUserWhithAdminCredentials() throws Exception {
        loginAsAdmin();
        IcesiUserRequestDTO icesiUserCreateDTO = defaultIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                mapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        IcesiUserResponseDTO icesiUserResponseDTO = mapper.readValue(result.getResponse().getContentAsString(), IcesiUserResponseDTO.class);
        assertNotNull(icesiUserResponseDTO);
    }

    private TokenDTO login(String username, String password) throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/token").content(
                                mapper.writeValueAsString(new LoginDTO(username,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = mapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
        assertNotNull(token);
        return token;
    }
    private void loginAsAdmin() throws Exception {
        tokenDTO = login("johndoe.admin@hotmail.com", "password");
    }
    private void loginAsUser() throws Exception {
        tokenDTO = login("johndoe.user@hotmail.com", "password");
    }
    private void loginAsBank() throws Exception {
        tokenDTO = login("johndoe.bank@hotmail.com", "password");
    }

    private IcesiUserRequestDTO defaultIcesiUserDTO(){
        return IcesiUserRequestDTO.builder()
                .firstName("Mariana")
                .lastName("Trujillo")
                .email("mariana.trujillo@hotmail.com")
                .phoneNumber("3147778889")
                .password("password")
                .role("USER")
                .build();
    }
}
