package co.edu.icesi.tallerjpa.controller.integration;

import co.edu.icesi.tallerjpa.TestConfigurationData;
import co.edu.icesi.tallerjpa.api.IcesiAccountApi;
import co.edu.icesi.tallerjpa.dto.LoginDTO;
import co.edu.icesi.tallerjpa.dto.TokenDTO;
import co.edu.icesi.tallerjpa.dto.TransactionCreateDTO;
import co.edu.icesi.tallerjpa.dto.TransactionResultDTO;
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

import static co.edu.icesi.tallerjpa.util.DTOBuilder.defaultTransferTransactionCreateDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class IcesiAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    TokenDTO tokenDTO;

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

    @Test
    public void testForEndpointForTransferringMoneyWithUserCredentials() throws Exception {
        loginAsUser();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                .content(objectMapper.writeValueAsString(transactionCreateDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TransactionResultDTO transactionResultDTO = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionResultDTO.class);

        assertEquals(transactionResultDTO.getResult(), "The transfer was successful");
        assertEquals(transactionResultDTO.getSenderAccountNumber(), transactionCreateDTO.getSenderAccountNumber());
        assertEquals(transactionResultDTO.getReceiverAccountNumber(), transactionCreateDTO.getReceiverAccountNumber());
        assertEquals(150, transactionResultDTO.getAmount());
    }
}
