package co.com.icesi.TallerJpa.integration.controller;


import co.com.icesi.TallerJpa.controller.api.IcesiAccountApi;
import co.com.icesi.TallerJpa.dto.LoginDTO;
import co.com.icesi.TallerJpa.dto.TokenDTO;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.integration.configuration.TestConfigurationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static co.com.icesi.TallerJpa.util.TestItemsBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class IcesiAccountControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    static TokenDTO user1TokenDTO;

    static TokenDTO user2TokenDTO;

    @BeforeEach
    public void init() {
        user1TokenDTO = loginAsUser1();
        user2TokenDTO = loginAsUser2();
    }

    @Test
    @DisplayName("Transfer money when user is not authenticated.")
    public void testTranferMoneyWhenNotAuthenticated() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .content(mapper.writeValueAsString(defaultTranferDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfUnauthorizedIsInTheResponse(icesiError);
    }
    @Test
    @DisplayName("Deposit money when user is not authenticated.")
    public void testDepositMoneyWhenNotAuthenticated() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/deposit")
                        .content(mapper.writeValueAsString(defaultDepositDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfUnauthorizedIsInTheResponse(icesiError);
    }
    @Test
    @DisplayName("Withdrawal money with user is not authenticated.")
    public void testWithdrawMoneyWhenNotAuthenticated() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/withdraw")
                        .content(mapper.writeValueAsString(defaultWithdrawDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfUnauthorizedIsInTheResponse(icesiError);
    }

    @Test
    @DisplayName("Transfer money with user credentials and Account Origin is Null.")
    public void testTransferMoneyWithUserCredentialsAndOriginNull() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAccountNumberOrigin(null);
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field accountNumberOrigin can't be null.");
    }

    @Test
    @DisplayName("Transfer money with user credentials and Account Origin is blank.")
    public void testTransferMoneyWithUserCredentialsAnOriginBlank() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAccountNumberOrigin(" ");
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field accountNumberOrigin can't be blank.");
    }

    @Test
    @DisplayName("Transfer money with user credentials and Account Origin is empty.")
    public void testTransferMoneyWithUserCredentialsAnOriginEmpty() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAccountNumberOrigin("");
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field accountNumberOrigin can't be empty.");
    }

    @Test
    @DisplayName("Transfer money with user credentials and Ammount Negative.")
    public void testTransferMoneyWithUserCredentialsAndAmmountNegative() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAmount(-1L);
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field amount El monto de una transaccion no puede ser menor a 0.");
    }
    @Test
    @DisplayName("Transfer money with user credentials Happy Path.")
    public void testTransferMoneyWithUserCredentialsHappyPath() throws Exception {
        var transfer = defaultTranferDTO();
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Transfer money with user credentials from Normal to Deposit.")
    public void testTransferMoneyWithUserCredentialsFromNormalToDeposit() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAccountNumberDestiny(user2Deposit());
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorIsInTheResponse(icesiError, "field AccountType is deposit only");
    }

    @Test
    @DisplayName("Transfer money with user credentials from Deposit to Normal.")
    public void testTransferMoneyWithUserCredentialsFromDepositToNormal() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAccountNumberOrigin(user1Deposit());
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorIsInTheResponse(icesiError, "field AccountType is deposit only");
    }

    @Test
    @DisplayName("Transfer money with user credentials from Deposit to Deposit.")
    public void testTransferMoneyWithUserCredentialsFromDepositToDeposit() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAccountNumberOrigin(user1Deposit());
        transfer.setAccountNumberDestiny(user2Deposit());
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorIsInTheResponse(icesiError, "field AccountType is deposit only");
    }

    @Test
    @DisplayName("Transfer money with user credentials not enough money.")
    public void testTransferMoneyWithUserCredentialsNotEnoughMoney() throws Exception {
        var transfer = defaultTranferDTO();
        transfer.setAmount(10000L);
        var result = mvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ACCOUNT_BASE_URL+"/transfer")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+user1TokenDTO.getToken())
                        .content(mapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorIsInTheResponse(icesiError, "field Balance has not enough money");
    }

    private TokenDTO loginAsUser1() {
        return login("johndoe.user@hotmail.com");
    }
    private TokenDTO loginAsUser2() {
        return login("janedoe.user@hotmail.com");
    }

    private TokenDTO login(String username) {
        try{
            var result = mvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    mapper.writeValueAsString(new LoginDTO(username, "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString());
            TokenDTO token = mapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
            assertNotNull(token);
            return token;
        }catch (Exception e ){
            return null;
        }
    }

    private void checkIfUnauthorizedIsInTheResponse(IcesiError icesiError){
        assertEquals(HttpStatus.UNAUTHORIZED, icesiError.getStatus());
        boolean e = icesiError.getDetails().stream()
                .anyMatch(error -> error.getErrorMessage().equals("Tienes que autenticarte primero."));
        assertTrue(e);
    }
    private void checkIfErrorIsInTheResponse(IcesiError icesiError, String expectedError){
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        boolean e = icesiError.getDetails().stream()
                .anyMatch(error -> error.getErrorMessage().equals(expectedError));
        assertTrue(e);
    }
}
