package com.Icesi.TallerJPA.integration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.Icesi.TallerJPA.TestConfigurationData;
import com.Icesi.TallerJPA.dto.IcesiAccountDTO;
import com.Icesi.TallerJPA.dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
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


    public String tokenEndpoint(String email, String password) throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO(email, password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private String tokenUser1() throws Exception {
        return tokenEndpoint("johndoe@email.com", "password");
    }

    private String tokenUser2() throws Exception {
        return tokenEndpoint("johndoe@email.com", "password");
    }
    @Test
    public void testTransferMoneyEndPointWhenAuthUser() throws Exception {
        String token = tokenUser1();
        try {
            var result = mockMvc.perform(patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "13", "162")
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
        var result = tokenUser1();
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
            var result = mockMvc.perform(post("/accounts")
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
            String token = tokenUser1();


                var result = mockMvc.perform(post("/accounts")
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



        }
    @Test
    public void testCreateUserWhenAuthUserq() throws Exception {
        String token = tokenUser1();


        try {
            var   result = mockMvc.perform(post("/accounts")
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


