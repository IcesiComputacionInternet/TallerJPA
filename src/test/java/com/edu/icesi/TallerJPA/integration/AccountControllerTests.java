package com.edu.icesi.TallerJPA.integration;

import com.edu.icesi.TallerJPA.TestConfigurationData;
import com.edu.icesi.TallerJPA.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    public class testsTransferMoneyHappyPath{

        @Test
        public void testTransferMoneyWithUser() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(TransactionDTO.builder()
                                            .sourceAccount("000000000")
                                            .destinationAccount("1234567")
                                            .amountMoney(2000L)
                                            .result("")
                                            .finalBalanceDestinationAccount(0L)
                                            .finalBalanceSourceAccount(0L)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            TransactionDTO transaction = objectMapper.readValue(newResult.getResponse().getContentAsString(), TransactionDTO.class);
            assertEquals(transaction.getFinalBalanceSourceAccount(),8000L);
            assertEquals(transaction.getFinalBalanceDestinationAccount(),17000L);
        }

        @Test
        public void testTransferMoneyWithAdmin() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(TransactionDTO.builder()
                                            .sourceAccount("000000000")
                                            .destinationAccount("1234567")
                                            .amountMoney(2000L)
                                            .result("")
                                            .finalBalanceDestinationAccount(0L)
                                            .finalBalanceSourceAccount(0L)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            TransactionDTO transaction = objectMapper.readValue(newResult.getResponse().getContentAsString(), TransactionDTO.class);
            assertEquals(transaction.getFinalBalanceSourceAccount(),8000L);
            assertEquals(transaction.getFinalBalanceDestinationAccount(),17000L);
        }


    }

    @Nested
    public class testForCreateUserNotHappyPath{

        @Test
        public void testTransferMoneyWithBankUser() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(TransactionDTO.builder()
                                            .sourceAccount("000000000")
                                            .destinationAccount("1234567")
                                            .amountMoney(2000L)
                                            .result("")
                                            .finalBalanceDestinationAccount(0L)
                                            .finalBalanceSourceAccount(0L)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }

        @Test
        public void testTransferMoneyToNoOwnAccount() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(TransactionDTO.builder()
                                            .sourceAccount("000000000")
                                            .destinationAccount("1357911")
                                            .amountMoney(2000L)
                                            .result("")
                                            .finalBalanceDestinationAccount(0L)
                                            .finalBalanceSourceAccount(0L)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }

        @Test
        public void testTransferMoneyToDepositOnlyAccount() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(TransactionDTO.builder()
                                            .sourceAccount("000000000")
                                            .destinationAccount("0246810")
                                            .amountMoney(2000L)
                                            .result("")
                                            .finalBalanceDestinationAccount(0L)
                                            .finalBalanceSourceAccount(0L)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 400);
        }

        @Test
        public void testTransferMoneyWithInsufficientMoney() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(TransactionDTO.builder()
                                            .sourceAccount("000000000")
                                            .destinationAccount("1234567")
                                            .amountMoney(12000L)
                                            .result("")
                                            .finalBalanceDestinationAccount(0L)
                                            .finalBalanceSourceAccount(0L)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 400);
        }
    }
}
