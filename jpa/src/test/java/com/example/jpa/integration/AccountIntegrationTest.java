package com.example.jpa.integration;

import com.example.jpa.TestConfigurationData;
import com.example.jpa.dto.RoleDTO;
import com.example.jpa.dto.TransactionRequestDTO;
import com.example.jpa.dto.UserDTO;
import com.example.jpa.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${security.token.admin}")
    private String tokenAdmin;
    @Value("${security.token.user}")
    private String tokenUser;
    @Value("${security.token.bank}")
    private String tokenBank;

    @Test
    void contextLoads() {
    }

    @Test
    public void testAccountEndpointWhenUserNotAuth() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/createAccount").content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testAccountEndpointWhenUserAuthAdminToCreateAccount() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/createAccount").content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testAccountEndpointWhenUserAuthUserToCreateAccount() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/createAccount").content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testAccountEndpointWhenUserAuthBankToCreateAccount() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/createAccount").content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testAccountEndpointWhenUserAuthAdminToGetAccount() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testAccountEndpointWhenUserAuthAdminToGetAllAccounts() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
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
    public void testTransactionDTOWhenAmountIsNegative() throws Exception {
        var result = mockMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionRequestDTO.builder()
                                        .amount(-1000L)
                                        .sourceAccount("123456789")
                                        .targetAccount("987654321")
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
    public void testTransactionDTOWhenAmountIsBlank() throws Exception {
        var result = mockMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionRequestDTO.builder()
                                        .amount(null)
                                        .sourceAccount("123456789")
                                        .targetAccount("987654321")
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

    private TransactionRequestDTO defaultTransactionRequestDTO() {
        return TransactionRequestDTO.builder()
                .amount(1000L)
                .sourceAccount("123456789")
                .targetAccount("987654321")
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

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .name("SUPERADMIN")
                .description("Ninguna")
                .build();
    }

    private RoleDTO defaultAdminRoleDTO() {
        return RoleDTO.builder()
                .name("ADMIN")
                .description("Admin role for test")
                .build();
    }

    private UserDTO defaultUserDTO() {
        return UserDTO.builder()
                .firstName("Santiago")
                .lastName("Arevalo")
                .password("1234567")
                .email("santiarevalo@gmail.com")
                .phoneNumber("+573162499422")
                .role(defaultRoleDTO())
                .build();
    }

}
