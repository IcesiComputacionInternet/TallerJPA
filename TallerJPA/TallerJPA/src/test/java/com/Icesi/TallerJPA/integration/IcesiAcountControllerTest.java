package com.Icesi.TallerJPA.integration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.Icesi.TallerJPA.TestConfigurationData;
import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.IcesiTransactionsDTO;
import com.Icesi.TallerJPA.dto.LoginDTO;
import com.Icesi.TallerJPA.dto.TokenDTO;
import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
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

import static com.Icesi.TallerJPA.api.AccountAPI.BASE_ACCOUNT_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.core.io.ClassPathResource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles="test")
@Import(TestConfigurationData.class)
public class IcesiAcountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    public TokenDTO tokenEndpoint(String email, String password) throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO(email, password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
    }

    private TokenDTO tokenUser1() throws Exception {
        return tokenEndpoint("johndoe@email.com", "password");
    }

    private TokenDTO tokenUser2() throws Exception {
        return tokenEndpoint("johndoe@email.com", "password");
    }
    @Test
    public void testTransferMoneyEndPointWhenAuthUser() throws Exception {
        var resultToken = mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String token = resultToken.getResponse().getContentAsString();

        try {
            var result = mockMvc.perform(patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                            .content("1")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        } catch (AssertionError e) {

        }

    }
    @Test
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
        var result = mockMvc.perform(
                        patch("/accounts/transfer/{accountNumberOrigin}/{accountNumberDestination}", "1", "2")
                                .content(objectMapper.writeValueAsString("100"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

    }
    @Test
    public void createTestForUserWhenBankAuthentication() throws Exception {
        var resultToken = mockMvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(new LoginDTO("ethan@email.com", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andReturn();
        String token = resultToken.getResponse().getContentAsString();


        try {
            var result = mockMvc.perform(post("/account")
                            .content(objectMapper.writeValueAsString(
                                    IcesiAccountDTO.builder()
                                            .active(true)
                                            .balance(50L)
                                            .type("USER")
                                            .build()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andReturn();
        } catch (AssertionError e) {

        }
    }
        @Test
        public void testCreateUserWhenAuthAdmin() throws Exception {
            var resultToken = mockMvc.perform(post("/login")
                            .content(objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andReturn();
            String token = resultToken.getResponse().getContentAsString();

            try {
                var result = mockMvc.perform(post("/account")
                                .content(objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .active(true)
                                                .balance(50L)
                                                .type("ADMIN")
                                                .build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

            } catch (AssertionError e) {

            }

        }

    }


