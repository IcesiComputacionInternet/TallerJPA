package co.edu.icesi.tallerjpa.controller.integration;

import co.edu.icesi.tallerjpa.TestConfigurationData;
import co.edu.icesi.tallerjpa.api.IcesiUserApi;
import co.edu.icesi.tallerjpa.dto.IcesiUserCreateDTO;
import co.edu.icesi.tallerjpa.dto.IcesiUserShowDTO;
import co.edu.icesi.tallerjpa.dto.LoginDTO;
import co.edu.icesi.tallerjpa.dto.TokenDTO;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
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

import java.util.Objects;

import static co.edu.icesi.tallerjpa.util.DTOBuilder.adminIcesiUserCreateDTO;
import static co.edu.icesi.tallerjpa.util.DTOBuilder.defaultIcesiUserCreateDTO;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class IcesiUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    TokenDTO tokenDTO;

    @Autowired
    IcesiUserRepository icesiUserRepository;

    private TokenDTO login(String username, String password) throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO(username,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
        assertNotNull(token);
        return token;
    }

    private void loginAsAdmin() throws Exception {
        tokenDTO = login("johndoe@email.com", "password");
    }

    private void loginAsUser() throws Exception {
        tokenDTO = login("johndoe2@email.com", "password");
    }

    private void loginAsBank() throws Exception {
        tokenDTO = login("johndoe3@email.com", "password");
    }

    private void checkThatIcesiUsersDTOsAreTheSame(IcesiUserCreateDTO icesiUserCreateDTO, IcesiUserShowDTO icesiUserShowDTO){
        assertNotNull(icesiUserShowDTO.getUserId());
        assertTrue(Objects.equals(icesiUserCreateDTO.getFirstName(), icesiUserShowDTO.getFirstName()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getLastName(), icesiUserShowDTO.getLastName()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getEmail(), icesiUserShowDTO.getEmail()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getPhoneNumber(), icesiUserShowDTO.getPhoneNumber()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getIcesiRoleCreateDTO().getDescription(), icesiUserShowDTO.getIcesiRole().getDescription()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getIcesiRoleCreateDTO().getName(), icesiUserShowDTO.getIcesiRole().getName()));
        icesiUserRepository.deleteById(icesiUserShowDTO.getUserId());
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentials() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
          objectMapper.writeValueAsString(icesiUserCreateDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
        checkThatIcesiUsersDTOsAreTheSame(icesiUserCreateDTO, icesiUserShowDTO);
    }

    @Test
    public void testEndpointForCreateAUserWithBankCredentials() throws Exception {
        loginAsBank();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
        checkThatIcesiUsersDTOsAreTheSame(icesiUserCreateDTO, icesiUserShowDTO);
    }

    @Test
    public void testEndpointForCreateAnAdminWithAdminCredentials() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = adminIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
        checkThatIcesiUsersDTOsAreTheSame(icesiUserCreateDTO, icesiUserShowDTO);
    }

//    @Test
//    public void testEndpointForCreateAnAdminWithBankCredentials() throws Exception {
//        loginAsBank();
//        IcesiUserCreateDTO icesiUserCreateDTO = adminIcesiUserCreateDTO();
//        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
//                        .content(
//                                objectMapper.writeValueAsString(icesiUserCreateDTO))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
//        assertNotNull(icesiUserShowDTO.getUserId());
//        assertTrue(Objects.equals(icesiUserCreateDTO.getFirstName(), icesiUserShowDTO.getFirstName()));
//        assertTrue(Objects.equals(icesiUserCreateDTO.getLastName(), icesiUserShowDTO.getLastName()));
//        assertTrue(Objects.equals(icesiUserCreateDTO.getEmail(), icesiUserShowDTO.getEmail()));
//        assertTrue(Objects.equals(icesiUserCreateDTO.getPhoneNumber(), icesiUserShowDTO.getPhoneNumber()));
//        assertTrue(Objects.equals(icesiUserCreateDTO.getIcesiRoleCreateDTO().getDescription(), icesiUserShowDTO.getIcesiRole().getDescription()));
//        assertTrue(Objects.equals(icesiUserCreateDTO.getIcesiRoleCreateDTO().getName(), icesiUserShowDTO.getIcesiRole().getName()));
//    }
}
