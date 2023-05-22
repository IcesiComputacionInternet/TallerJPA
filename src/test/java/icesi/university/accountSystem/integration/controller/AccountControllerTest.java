package icesi.university.accountSystem.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import icesi.university.accountSystem.TestConfigurationData;
import icesi.university.accountSystem.dto.LoginDTO;
import icesi.university.accountSystem.dto.TokenDTO;
import icesi.university.accountSystem.dto.TransactionOperationDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private String token;

    private String getToken(String username, String password) throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post("/token").content(
                                mapper.writeValueAsString(new LoginDTO(username,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
            JSONObject json = new JSONObject((result.getResponse().getContentAsString()));
            return json.getString("token");
    }

    private void loginAsAdmin() throws Exception {
        token = getToken("johndoe@gmail.com", "password");
    }

    private void loginAsUser() throws Exception {
        token = getToken("johndoe2@email.com", "password");
    }

    private void loginAsAnotherUser() throws Exception {
        token = getToken("johndoe4@email.com", "password");
    }

    private void loginAsBank() throws Exception {
        token = getToken("johndoe3@email.com", "password");
    }
    @Test
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
        var result = mvc.perform(patch("/accounts/transfer")
                        .content(mapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(1000L)
                                        .accountFrom("123-456789-90")
                                        .accountTo("123-456789-99")
                                        .build()
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenUserIsAuth() throws Exception {
        loginAsUser();
        var result = mvc.perform(patch("/accounts/transfer")
                        .content(mapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(1000L)
                                        .accountFrom("123-456789-90")
                                        .accountTo("123-456789-99")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionDTOWhenDontHaveNumberOrigin() throws Exception {
        loginAsUser();
        var result = mvc.perform(patch("/accounts/transfer")
                        .content(mapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(1000L)
                                        .accountTo("123-456789-99")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionDTOWhenAmountIsNegative() throws Exception {
        loginAsUser();
        var result = mvc.perform(patch("/accounts/transfer")
                        .content(mapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(-1000L)
                                        .accountFrom("123-456789-90")
                                        .accountTo("123-456789-99")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
