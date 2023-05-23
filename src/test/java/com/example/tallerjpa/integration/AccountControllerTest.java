package com.example.tallerjpa.integration;

import com.example.tallerjpa.dto.AccountDTO;
import com.example.tallerjpa.dto.LoginDTO;
import com.example.tallerjpa.dto.TokenDTO;
import com.example.tallerjpa.dto.TransactionRequestDTO;
import com.example.tallerjpa.enums.AccountType;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfigurationData.class )
@ActiveProfiles(profiles = "test")
@SpringBootTest
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token = "";

    private static final String URL = "/accounts";

    @Test
    public void createAccount() throws Exception {
        setToken();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(AccountDTO.builder()
                                        .balance(100L)
                                        .type(AccountType.DEFAULT)
                                        .emailUser("juan@hotmail.com")
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void transferMoneyWhenUserIsAuth() throws Exception {
        setToken();
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL+"/transfer/").content(
                                objectMapper.writeValueAsString(defaultTransaction()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void transferMoneyWhenTheAccountIsOnlyDeposits() throws Exception {
        setToken();
        TransactionRequestDTO transaction = defaultTransaction();
        transaction.setOriginAccountNumber("111-567788-33");
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL+"/transfer/").content(
                                objectMapper.writeValueAsString(transaction))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void transferMoneyWithLowBalance() throws Exception {
        setToken();
        TransactionRequestDTO transaction = defaultTransaction();
        transaction.setAmount(20000L);
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL+"/transfer/").content(
                                objectMapper.writeValueAsString(transaction))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    public void  setToken() throws Exception {
        var login =   mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(LoginDTO.builder().username("bank@email.com").password("password").build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO tokenDTO = objectMapper.readValue(login.getResponse().getContentAsString(), TokenDTO.class);
        token = tokenDTO.getToken();
    }

    private TransactionRequestDTO defaultTransaction(){
        return TransactionRequestDTO.builder()
                .originAccountNumber("098-765432-11")
                .destinationAccountNumber("123-456789-00")
                .amount(100L)
                .build();
    }
}
