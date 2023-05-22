package co.edu.icesi.tallerjpa.runableartefact.integration.controller;

import co.edu.icesi.tallerjpa.runableartefact.dto.LoginDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiAccountDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.IcesiUserDTO;
import co.edu.icesi.tallerjpa.runableartefact.dto.request.TransactionInformationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
class RunableartefactApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    void tokenCreation() throws Exception {
        token = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                        objectMapper.writeValueAsString(new LoginDTO("test@email.com", "password"))
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
    }

    void unauthorizedTokenCreation() throws Exception{
        token = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                        objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
    }
    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testTokenEndpoint() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("test@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = result.getResponse().getContentAsString();
        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTokenEndpointWithInvalidCredentials() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("invalid", "invalid"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUser() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("holi2@email.com")
                                                .password("password")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("USER")
                                                .phoneNumber("3176162821")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUserWithInvalidEmail() throws Exception {
        unauthorizedTokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("invalid")
                                                .password("password")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("USER")
                                                .phoneNumber("3176162821")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUserWithInvalidRole() throws Exception {
        unauthorizedTokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("invalid")
                                                .password("password")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("invalid")
                                                .phoneNumber("3176162821")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUserWithInvalidPhoneNumber() throws Exception {
        unauthorizedTokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("invalid")
                                                .password("password")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("USER")
                                                .phoneNumber("invalid")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveNewIcesiUserWithInvalidPassword() throws Exception {
        unauthorizedTokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-users/save")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("invalid")
                                                .password("invalid")
                                                .firstName("test")
                                                .lastName("test")
                                                .roleName("USER")
                                                .phoneNumber("3176162821")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateNewAccount() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-accounts/create")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .active(true)
                                                .balance(0L)
                                                .type("Ahorrros")
                                                .icesiUserEmail("")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateNewAccountWithInvalidType() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-accounts/create")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .active(true)
                                                .balance(0L)
                                                .type("invalid")
                                                .icesiUserEmail("")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateNewAccountWithInvalidBalance() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-accounts/create")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .active(true)
                                                .balance(-1L)
                                                .type("Ahorros")
                                                .icesiUserEmail("")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateNewAccountWithInvalidEmail() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/icesi-accounts/create")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .active(true)
                                                .balance(0L)
                                                .type("Ahorros")
                                                .icesiUserEmail("invalid")
                                                .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //No longer takes into account the email, now uses the actual user
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testWitdrawal() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/withdrawal")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .amount(1000L)
                                        .accountNumberOrigin("123456789")
                                        .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //No longer takes into account the email, now uses the actual user
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testDeposit() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/deposit")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .amount(1000L)
                                        .accountNumberOrigin("123456789")
                                        .build()
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //No longer takes into account the email, now uses the actual user
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransfer() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/transfer")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .amount(1000L)
                                        .accountNumberOrigin("123456789")
                                        .accountNumberDestination("987654321")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //No longer takes into account the email, now uses the actual user
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testActivateAccount() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/activate")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .accountNumberOrigin("1122334456")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //No longer takes into account the email, now uses the actual user
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testActivateAccountWithInvalidAccountNumber() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/activate")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .accountNumberOrigin("987654321")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testDeactivate() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/deactivate")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .accountNumberOrigin("1122334455")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //No longer takes into account the email, now uses the actual user
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testDeactivateAccountWithInvalidAccountNumber() throws Exception {
        tokenCreation();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/icesi-accounts/deactivate")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(
                                TransactionInformationDTO.builder()
                                        .accountNumberOrigin("987654321")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.err.println(result.getResponse().getContentAsString());
    }

}
