package co.edu.icesi.demo.integration;

import co.edu.icesi.demo.TestConfigurationData;
import co.edu.icesi.demo.dto.LoginDTO;
import co.edu.icesi.demo.dto.TokenDTO;
import co.edu.icesi.demo.dto.TransactionDTO;
import co.edu.icesi.demo.error.exception.IcesiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static co.edu.icesi.demo.api.AccountAPI.BASE_ACCOUNT_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles="test")
@Import(TestConfigurationData.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public TokenDTO tokenEndpoint(String email, String password) throws Exception{
        var result= mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO(email,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
    }

    private TokenDTO tokenUser1()throws Exception{
        return tokenEndpoint("johndoe2@email.com","password");
    }

    private TokenDTO tokenUser2()throws Exception{
        return tokenEndpoint("johndoe123@email.com","password");
    }

    @Order(1)
    @Test
    public void testTransferMoneyWhenComesFromDepositOnlyAccount() throws Exception{

        TokenDTO tokenDTO=tokenUser1();

        var result=mockMvc.perform(MockMvcRequestBuilders.put(BASE_ACCOUNT_URL+"/transfer").content(
                                objectMapper.writeValueAsString(transactionDTOWithAccountDepositOnlyFrom())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Deposit only account can't transfer or be transferred money: Account Number 123-123456-22",detail.getErrorMessage());
    }

    @Order(2)
    @Test
    public void testTransferMoneyWhenGoesToDepositOnlyAccount() throws Exception{

        TokenDTO tokenDTO=tokenUser1();

        var result=mockMvc.perform(MockMvcRequestBuilders.put(BASE_ACCOUNT_URL+"/transfer").content(
                                objectMapper.writeValueAsString(transactionDTOWithAccountDepositOnlyTo())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Deposit only account can't transfer or be transferred money: Account Number 123-123456-22",detail.getErrorMessage());
    }

    @Order(3)
    @Test
    public void testTransferMoneyWhenAmountGreaterThanBalance() throws Exception{

        TokenDTO tokenDTO=tokenUser1();

        var result=mockMvc.perform(MockMvcRequestBuilders.put(BASE_ACCOUNT_URL+"/transfer").content(
                                objectMapper.writeValueAsString(transactionDTOWithAccountFromInitialBalanceInZero())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Not enough money in the account to do this transaction ",detail.getErrorMessage());
    }

    @Order(4)
    @Test
    public void testTransferMoneyUnauthorizedUser() throws Exception{

        TokenDTO tokenDTO=tokenUser2();

        var result=mockMvc.perform(MockMvcRequestBuilders.put(BASE_ACCOUNT_URL+"/transfer").content(
                                objectMapper.writeValueAsString(defaultTransactionDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isForbidden())
                .andReturn();
        IcesiError icesiError =objectMapper.readValue(result.getResponse().getContentAsString(),IcesiError.class);
        assertNotNull(icesiError);
        var details = icesiError.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Unauthorized: User cannot perform this transaction",detail.getErrorMessage());
    }

    @Order(5)
    @Test
    public void testTransferMoney() throws Exception{

        TokenDTO tokenDTO=tokenUser1();

        var result=mockMvc.perform(MockMvcRequestBuilders.put(BASE_ACCOUNT_URL+"/transfer").content(
                                objectMapper.writeValueAsString(defaultTransactionDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        TransactionDTO transactionDTO =objectMapper.readValue(result.getResponse().getContentAsString(),TransactionDTO.class);
        assertNotNull(transactionDTO);
        assertEquals("Transfer successfully completed",transactionDTO.getResult());
    }

    private TransactionDTO defaultTransactionDTO(){
        return TransactionDTO.builder()
                .accountNumberFrom("123-123456-23")
                .accountNumberTo("123-123456-21")
                .money(100)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountDepositOnlyFrom(){
        return TransactionDTO.builder()
                .accountNumberFrom("123-123456-22")
                .accountNumberTo("123-123456-21")
                .money(100)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountDepositOnlyTo(){
        return TransactionDTO.builder()
                .accountNumberFrom("123-123456-23")
                .accountNumberTo("123-123456-22")
                .money(100)
                .build();
    }

    private TransactionDTO transactionDTOWithAccountFromInitialBalanceInZero(){
        return TransactionDTO.builder()
                .accountNumberFrom("123-123456-21")
                .accountNumberTo("123-123456-23")
                .money(100)
                .build();
    }


}
