package co.edu.icesi.tallerjpa.controller.integration;

import co.edu.icesi.tallerjpa.TestConfigurationData;
import co.edu.icesi.tallerjpa.api.IcesiAccountApi;
import co.edu.icesi.tallerjpa.dto.LoginDTO;
import co.edu.icesi.tallerjpa.dto.TokenDTO;
import co.edu.icesi.tallerjpa.dto.TransactionCreateDTO;
import co.edu.icesi.tallerjpa.dto.TransactionResultDTO;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiErrorDetail;
import co.edu.icesi.tallerjpa.repository.IcesiAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static co.edu.icesi.tallerjpa.util.DTOBuilder.defaultTransferTransactionCreateDTO;
import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    IcesiAccountRepository icesiAccountRepository;

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


    public void reloadDefaultValues(){
        icesiAccountRepository.updateBalance(1500, UUID.fromString("aa14c92e-7505-4fe3-8bb7-2f418504e867"));
        icesiAccountRepository.updateBalance(650, UUID.fromString("cc62238e-ce1b-4026-9ab5-2f47944dd150"));
    }

    @Test
    @Transactional
    public void testForEndpointForTransferringMoneyWithUserCredentialsAndWithHisAccount() throws Exception {
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

        assertEquals("The transfer was successful", transactionResultDTO.getResult());
        assertEquals(transactionResultDTO.getSenderAccountNumber(), transactionCreateDTO.getSenderAccountNumber());
        assertEquals(transactionResultDTO.getReceiverAccountNumber(), transactionCreateDTO.getReceiverAccountNumber());
        assertEquals(150, transactionResultDTO.getAmount());
        reloadDefaultValues();
    }

    @Test
    @Transactional
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithHisAccount() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("222-123456-22")
                .receiverAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TransactionResultDTO transactionResultDTO = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionResultDTO.class);

        assertEquals("The transfer was successful", transactionResultDTO.getResult());
        assertEquals(transactionResultDTO.getSenderAccountNumber(), transactionCreateDTO.getSenderAccountNumber());
        assertEquals(transactionResultDTO.getReceiverAccountNumber(), transactionCreateDTO.getReceiverAccountNumber());
        assertEquals(1350, transactionResultDTO.getAmount());
        reloadDefaultValues();
    }

    @Test
    public void testForEndpointForTransferringMoneyWithBankCredentials() throws Exception {
        loginAsBank();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("222-123456-22")
                .receiverAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    @Test
    @Transactional
    public void testForEndpointForTransferringMoneyWithUserCredentialsAndWithTheAccountOfOther() throws Exception {
        loginAsUser();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("222-123456-22")
                .receiverAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.FORBIDDEN, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_403.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals(icesiErrorDetailList.get(0).getErrorMessage(), "The account does not belong to johndoe2@email.com");
        reloadDefaultValues();
    }

    @Test
    @Transactional
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithTheAccountOfOther() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TransactionResultDTO transactionResultDTO = objectMapper.readValue(result.getResponse().getContentAsString(), TransactionResultDTO.class);

        assertEquals("The transfer was successful", transactionResultDTO.getResult());
        assertEquals(transactionResultDTO.getSenderAccountNumber(), transactionCreateDTO.getSenderAccountNumber());
        assertEquals(transactionResultDTO.getReceiverAccountNumber(), transactionCreateDTO.getReceiverAccountNumber());
        assertEquals(150, transactionResultDTO.getAmount());
        reloadDefaultValues();
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithDisableSenderAccount() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("333-123456-33")
                .receiverAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_400.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals("field isActive: The account " + transactionCreateDTO.getSenderAccountNumber() + " is disabled", icesiErrorDetailList.get(0).getErrorMessage());
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithDisableReceiverAccount() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .receiverAccountNumber("333-123456-33")
                .senderAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_400.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals("field isActive: The account " + transactionCreateDTO.getReceiverAccountNumber() + " is disabled", icesiErrorDetailList.get(0).getErrorMessage());
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithDepositOnlyReceiverAccount() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .receiverAccountNumber("444-123456-44")
                .senderAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_400.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals("field type: The account with number " + transactionCreateDTO.getReceiverAccountNumber() + " is marked as deposit only so no money can be transferred", icesiErrorDetailList.get(0).getErrorMessage());
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithDepositOnlySenderAccount() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("444-123456-44")
                .receiverAccountNumber("111-123456-11")
                .amount(150)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_400.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals("field type: The account with number " + transactionCreateDTO.getSenderAccountNumber() + " is marked as deposit only so no money can be transferred", icesiErrorDetailList.get(0).getErrorMessage());
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndWithNotEnoughMoney() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("111-123456-11")
                .receiverAccountNumber("222-123456-22")
                .amount(700)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_400.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals("field balance: Not enough money to transfer. At most you can transfer: 650", icesiErrorDetailList.get(0).getErrorMessage());
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndTheSameReceiverAndSenderAccountNumbers() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = TransactionCreateDTO.builder()
                .senderAccountNumber("111-123456-11")
                .receiverAccountNumber("111-123456-11")
                .amount(100)
                .build();
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(ErrorCode.ERR_400.getCode(), icesiErrorDetailList.get(0).getErrorCode());
        assertEquals("field senderAccountNumber and receiverAccountNumber: The sender account number and receiver account number must be different", icesiErrorDetailList.get(0).getErrorMessage());
    }

    private void checkIfErrorDetailIsInTheList(IcesiError icesiError, String errorMessage){
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        boolean foundTheErrorMessage = false;
        for(IcesiErrorDetail icesiErrorDetail: icesiErrorDetailList){
            assertEquals(icesiErrorDetail.getErrorCode(), ErrorCode.ERR_400.getCode());
            if(icesiErrorDetail.getErrorMessage().equals(errorMessage)){
                foundTheErrorMessage = true;
                break;
            }
        }
        assertTrue(foundTheErrorMessage);
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndNullSenderAccountNumber() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setSenderAccountNumber(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field senderAccountNumber: The sender account can not be null");

    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndInvalidSenderAccountNumber() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setSenderAccountNumber("11111");
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field senderAccountNumber: Invalid format of account number");

    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndNullReceiverAccountNumber() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setReceiverAccountNumber(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field receiverAccountNumber: The receiver account can not be null");

    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndInvalidReceiverAccountNumber() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setReceiverAccountNumber("11111");
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field receiverAccountNumber: Invalid format of account number");
    }

    @Test
    public void testForEndpointForTransferringMoneyWithAdminCredentialsAndNegativeAmount() throws Exception {
        loginAsAdmin();
        TransactionCreateDTO transactionCreateDTO = defaultTransferTransactionCreateDTO();
        transactionCreateDTO.setAmount(-1);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch(IcesiAccountApi.ROOT_PATH+"/transfer_money")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(objectMapper.writeValueAsString(transactionCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field amount: The min value for the balance is 0");

    }
}
