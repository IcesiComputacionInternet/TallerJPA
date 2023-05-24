package co.com.icesi.demojpa.integration;



import co.com.icesi.demojpa.TestConfigurationData;
import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TokenDTO;
import co.com.icesi.demojpa.dto.TransactionDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.error.exception.IcesiError;
import co.com.icesi.demojpa.error.exception.IcesiException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class AccountTest{

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    @Test
    public void testTransferEndpointHappyPath() throws Exception {
        token=mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
                                objectMapper.writeValueAsString(TransactionDTO.builder()
                                        .accountNumberOrigin("1234567899")
                                        .accountNumberDestination("123456789")
                                        .amount(1000L)
                                        .resultMessage("")
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        TransactionDTO transactionDTO = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), TransactionDTO.class);
        assertEquals("El nuevo balance es de: 999000", transactionDTO.getResultMessage());
    }

    @Test
    public void testTransferEndpointBankRole() throws Exception {
        token= mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
                                objectMapper.writeValueAsString(TransactionDTO.builder()
                                        .accountNumberOrigin("1234567899")
                                        .accountNumberDestination("123456789")
                                        .amount(1000L)
                                        .resultMessage("")
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testTransferEndpointInValidAccNumber() throws Exception {
        token=mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
                                objectMapper.writeValueAsString(TransactionDTO.builder()
                                        .accountNumberOrigin("1234567899")
                                        .accountNumberDestination("123456788")
                                        .amount(1000L)
                                        .resultMessage("")
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().is4xxClientError());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
        assertEquals("ACCOUNT_NOT_FOUND", error.getDetails());
    }

    @Test
    public void testTransferEndpointBalance() throws Exception {
        token=mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
                                objectMapper.writeValueAsString(TransactionDTO.builder()
                                        .accountNumberOrigin("1234567899")
                                        .accountNumberDestination("123456789")
                                        .amount(10000000000000L)
                                        .resultMessage("")
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().is5xxServerError());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
        assertEquals("La cuenta que manda el dinero no tiene balance suficiente ", error.getDetails());
    }


}

