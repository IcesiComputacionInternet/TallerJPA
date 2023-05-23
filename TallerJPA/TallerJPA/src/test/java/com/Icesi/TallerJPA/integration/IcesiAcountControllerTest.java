package com.Icesi.TallerJPA.integration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.Icesi.TallerJPA.TestConfigurationData;
import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.IcesiTransactionsDTO;
import com.Icesi.TallerJPA.dto.LoginDTO;
import com.Icesi.TallerJPA.dto.TokenDTO;
import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles="test")
@Import(TestConfigurationData.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IcesiAcountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    public TokenDTO generateToken(String email, String password) throws Exception{
        IcesiAccount account = defaultAccount();
        IcesiAccount account2 = defaultAccount2();
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO(email, password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
    }



        @Test
        public void testTransferWithUserUser() throws Exception{
            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(defaultTransaction())
                            )
                            .header("Authorization","Bearer "+generateToken("johndoe2@email.com","password").getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError())
                    .andReturn();


        }

        @Test
        public void testTransfeWithUserAdmin() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(defaultTransaction())
                            )
                            .header("Authorization","Bearer "+generateToken("johndoe@email.com","password").getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError())
                    .andReturn();



        }






        @Test
        public void testTransferToNoOwnAccount() throws Exception{
            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(defaultNotOwnTransaction())
                            )
                            .header("Authorization","Bearer "+generateToken("johndoe2@email.com","password").getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError())
                    .andReturn();


        }

        @Test
        public void testInvalidTransferInvalidTypeAccount() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(accountOnlyDeposit())
                            )
                            .header("Authorization","Bearer "+generateToken("johndoe2@email.com","password").getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError())
                    .andReturn();


        }

        @Test
        public void testTransferMoneyWithInsufficientMoney() throws Exception{

            IcesiTransactionsDTO transactionDTO = defaultTransaction();
            transactionDTO.setAmount(12000L);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                    objectMapper.writeValueAsString(transactionDTO)
                            )
                            .header("Authorization","Bearer "+generateToken("johndoe2@email.com","password").getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is5xxServerError())
                    .andReturn();


        }

    @Test
    public void testTransferWithUserBank() throws Exception{

        var newResult = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer/").content(
                                objectMapper.writeValueAsString(defaultTransaction())
                        )
                        .header("Authorization","Bearer "+generateToken("ethan@email.com","password").getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();


    }
    public IcesiTransactionsDTO defaultTransaction(){
        return IcesiTransactionsDTO.builder()
                .accountOrigin("1234567")
                .accountDestination("12345678")
                .amount(25000L)
                .build();
    }

    public IcesiTransactionsDTO defaultNotOwnTransaction(){
        return IcesiTransactionsDTO.builder()
                .accountOrigin("1234567")
                .accountDestination("12345678")
                .amount(5000L)

                .build();
    }

    public IcesiTransactionsDTO accountOnlyDeposit(){
        return IcesiTransactionsDTO.builder()
                .accountOrigin("1234567")
                .accountDestination("12345678")
                .amount(10000L)
                .build();
    }
    private IcesiAccount defaultAccount() {
        return IcesiAccount.builder()
                .accountNumber("1234567")
                .balance(50L)
                .type("Normal")
                .active(true)
                .icesiUser(defaultIcesiUser())
                .build();
    }
    private IcesiUser defaultIcesiUser(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a81201-0000-0000-0000-000000000000"))
                .firstName("Luis")
                .lastName("David")
                .email("example@exampleEmail.com")
                .phoneNumber("1234545")
                .password("1234125")
                .icesiRole(defaultRole())
                .build();

    }
    private IcesiRole defaultRole(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a81201-0000-0000-0000-000000000000"))
                .name("ADMIN")
                .description("Sistemas")
                .build();
    }
    private IcesiAccount defaultAccount2() {
        return IcesiAccount.builder()
                .accountNumber("12345678")
                .balance(50L)
                .type("Normal")
                .active(true)
                .icesiUser(defaultIcesiUser2())
                .build();
    }
    private IcesiUser defaultIcesiUser2(){
        return IcesiUser.builder()
                .userId(UUID.fromString("c0a80101-0000-0000-0000-000000200000"))
                .firstName("Luis")
                .lastName("David")
                .email("examplea@exampleEmail.com")
                .phoneNumber("1234545")
                .password("1234125")
                .icesiRole(defaultRole2())
                .build();
    }
    private IcesiRole defaultRole2(){
        return IcesiRole.builder()
                .roleId(UUID.fromString("c0a80101-0000-0000-0000-000000200000"))
                .name("ADMIN")
                .description("Sistemas")
                .build();
    }
    }


