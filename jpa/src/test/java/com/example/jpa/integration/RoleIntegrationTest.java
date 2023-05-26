package com.example.jpa.integration;

import com.example.jpa.TestConfigurationData;
import com.example.jpa.dto.LoginDTO;
import com.example.jpa.dto.RoleDTO;
import com.example.jpa.dto.UserDTO;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class RoleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenUser;
    private String tokenBank;

    private final static String CREATE_ROLE_URL = "/roles/createRole";

    @Test
    void contextLoads() {
    }

    @Test
    public void testRolesEndpointWhenUserNotAuth() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ROLE_URL).content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    //These test are to validate the creation of a role according to the User type that is authenticated
    //Besides these tests were done fulfilling the custom anotations and the input validations (@Valid) of RoleCreateDTO
    @Test
    public void testCreateRoleWhenUserAuthAdmin() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ROLE_URL).content(
                                objectMapper.writeValueAsString(defaultRoleDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateRoleWhenUserAuthUser() throws Exception {
        tokenUser = getTokenUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ROLE_URL).content(
                                objectMapper.writeValueAsString(defaultRoleDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateRoleWhenUserAuthBank() throws Exception {
        tokenUser = getTokenUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ROLE_URL).content(
                                objectMapper.writeValueAsString(defaultRoleDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test   //Test to fail the validation @NotBlank name of RoleCreateDTO
    public void testCreateBadRoleWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ROLE_URL).content(
                                objectMapper.writeValueAsString(badRole())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
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

    private RoleDTO badRole() {
        return RoleDTO.builder()
                .name("")
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
