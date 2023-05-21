package co.com.icesi.tallerjpa.integration.controller;

import co.com.icesi.tallerjpa.dto.TransactionDTO;
import co.com.icesi.tallerjpa.integration.configuration.TestConfigurationData;
import co.com.icesi.tallerjpa.model.Account;
import co.com.icesi.tallerjpa.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    @Value("${security.token.permanent}")
    private String key;
    @Test
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
        var result = mvc.perform(patch("/accounts/transfer")
                        .content(mapper.writeValueAsString(
                                TransactionDTO.builder()
                                        .amount(1000L)
                                        .accountNumberOrigin("123456789")
                                        .accountNumberDestination("987654321")
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
        var result = mvc.perform(patch("/accounts/transfer")
                        .content(mapper.writeValueAsString(
                                TransactionDTO.builder()
                                        .amount(1000L)
                                        .accountNumberOrigin("123456789")
                                        .accountNumberDestination("987654321")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionDTOWhenDontHaveNumberOrigin() throws Exception {
    	var result = mvc.perform(patch("/accounts/transfer")
    			.content(mapper.writeValueAsString(
    					TransactionDTO.builder()
    					.amount(1000L)
    					.accountNumberDestination("987654321")
    					.build()
    					))
    			.header("Authorization", "Bearer " + key)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    			.andExpect(status().isBadRequest())
    			.andReturn();

    	System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionDTOWhenAmountIsNegative() throws Exception {
    	var result = mvc.perform(patch("/accounts/transfer")
    			.content(mapper.writeValueAsString(
    					TransactionDTO.builder()
    					.amount(-1000L)
    					.accountNumberOrigin("123456789")
    					.accountNumberDestination("987654321")
    					.build()
    					))
    			.header("Authorization", "Bearer " + key)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			)
    			.andExpect(status().isBadRequest())
    			.andReturn();

    	System.out.println(result.getResponse().getContentAsString());
    }
}
