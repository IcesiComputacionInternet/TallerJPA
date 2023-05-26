package com.example.jpa.integration;

import com.example.jpa.TestConfigurationData;
import com.example.jpa.dto.*;
import com.example.jpa.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;


    private String tokenAdmin;
    private String tokenUser;
    private String tokenBank;

    private final static String CREATE_ACCOUNT_URL = "/accounts/createAccount";

    @Test
    void contextLoads() {
    }

    @Test
    public void testAccountEndpointWhenUserNotAuth() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ACCOUNT_URL).content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAccountWhenUserAuthAdmin() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ACCOUNT_URL).content(
                                objectMapper.writeValueAsString(defaultAccountRequestDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllAccountsWhenUserAuthAdmin() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyWhenUserIsNotAuth() throws Exception {
        var result = mockMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(defaultTransactionRequestDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenUserIsAuth() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(defaultTransactionRequestDTO()))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionWhenAmountIsNegative() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionRequestDTO.builder()
                                        .amount(-1000L)
                                        .sourceAccount("893-887868-67")
                                        .targetAccount("897-887868-67")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionWhenAmountIsBlank() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionRequestDTO.builder()
                                        .amount(null)
                                        .sourceAccount("893-887868-67")
                                        .targetAccount("897-887868-67")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    //Method to log in and get an admin token
    private String getTokenAdmin() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    //Method to log in and get a user token
    private String getTokenUser() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com","password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    //Method to log ing and get a bank token
    private String getTokenBank() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com","password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private TransactionRequestDTO defaultTransactionRequestDTO() {
        return TransactionRequestDTO.builder()
                .amount(5L)
                .sourceAccount("897-887868-67")
                .targetAccount("893-887868-67")
                .build();
    }

    private AccountRequestDTO defaultAccountRequestDTO() {
        return AccountRequestDTO.builder()
                .balance(1000L)
                .type("AHORROS")
                .user(defaultUserDTO())
                .build();
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .name("USER")
                .description("Ninguna")
                .build();
    }

    private RoleDTO defaultAdminRoleDTO() {
        return RoleDTO.builder()
                .name("ADMIN")
                .description("Admin role for test")
                .build();
    }

    private UserDTO defaultAdminDTO() {
        return UserDTO.builder()
                .firstName("Santiago")
                .lastName("Arevalo")
                .password("1234567")
                .email("santiarevaloadmin@gmail.com")
                .phoneNumber("+573162499422")
                .role(defaultAdminRoleDTO())
                .build();
    }

    private UserDTO defaultUserDTO() {
        return UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .email("johndoe2@email.com")
                .phoneNumber("+573174657863")
                .role(defaultRoleDTO())
                .build();
    }

}
