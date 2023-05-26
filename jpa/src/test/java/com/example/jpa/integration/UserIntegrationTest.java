package com.example.jpa.integration;

import com.example.jpa.TestConfigurationData;
import com.example.jpa.dto.LoginDTO;
import com.example.jpa.dto.RoleDTO;
import com.example.jpa.dto.UserDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String tokenAdmin;
    private String tokenUser;
    private String tokenBank;

    private final static String CREATE_USER_URL = "/users/createUser";
    private final static String GET_ALL_USERS_URL = "/users/all";

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateUserEndpoint() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    // Create an integration test for the endpoint /users/all
    // The test should return a list of users
    // The test should return a status 200
    @Test
    public void testGetAllUsersEndpoint() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    //Create an integration test fot the endpoint /users/{userId}/
    //The test should return a status 400 - User not found
    @Test
    public void testGetUserEndpoint() throws Exception {
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/users/a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().is4xxClientError())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void testCreateBankUserWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultBankDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAdminWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenUserAuthUser() throws Exception{
        tokenUser = getTokenUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testCreateBankUserWhenUserAuthUser() throws Exception{
        tokenUser = getTokenUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultBankDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAdminUserWhenUserAuthUser() throws Exception{
        tokenUser = getTokenUser();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenUserAuthBank() throws Exception{
        tokenBank = getTokenBank();
        System.out.println(tokenBank);
        System.out.println(defaultUserDTO().getRole().getName());
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAdminUserWhenUserAuthBank() throws Exception{
        tokenBank = getTokenBank();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateBankUserWhenUserAuthBank() throws Exception{
        tokenBank = getTokenBank();
        System.out.println(tokenBank);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(defaultBankDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    //These tests were done to fail the input validations (@Valid)

    @Test   //This test is to fail the input validation of the @NotBlank firstName
    public void testCreateUserWithoutFirtsNameWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @NotBlank lastName
    public void testCreateUserWithoutLastNameWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @Email email
    public void testCreateUserWithEmailBadFormatWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test   //This test is to fail the input validation of the @RegionPhoneNumberValidation phoneNumber
    public void testCreateUserWithPhoneBadFormatWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test   //This test is to fail the input validation of the @NotBlank password
    public void testCreateUserWithoutPasswordWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test   //This test is to fail the input validation of the @NotBlank role
    public void testCreateUserWithoutRoleWhenUserAuthAdmin() throws Exception{
        tokenAdmin = getTokenAdmin();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(CREATE_USER_URL).content(
                                objectMapper.writeValueAsString(userWithoutAnything())
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

    private UserDTO defaultUserDTO() {
        return UserDTO.builder()
                .firstName("Santiago")
                .lastName("Arevalo")
                .password("1234567")
                .email("santiarevalo@gmail.com")
                .phoneNumber("+573162499421")
                .role(defaultRoleDTO())
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

    private UserDTO defaultBankDTO() {
        return UserDTO.builder()
                .firstName("Santiago")
                .lastName("Arevalo")
                .password("1234567")
                .email("santiarevalobank@gmail.com")
                .phoneNumber("+573162499423")
                .role(defaultBankRoleDTO())
                .build();
    }

    private UserDTO userWithoutAnything(){
        return UserDTO.builder()
                .firstName("")
                .lastName("")
                .email("aghotmail.com")
                .phoneNumber("3151245687")
                .password("")
                .role(nullRole())
                .build();
    }

    private UserDTO userByBankCreateDTO(){
        return UserDTO.builder()
                .firstName("Camila")
                .lastName("Varela")
                .email("t1@hotmail.com")
                .phoneNumber("+573151220387")
                .password("1234")
                .role(defaultRoleDTO())
                .build();
    }

    private UserDTO bankByBankCreateDTO(){
        return UserDTO.builder()
                .firstName("Fanny")
                .lastName("Gomez")
                .email("t2@hotmail.com")
                .phoneNumber("+573150510559")
                .password("1234")
                .role(defaultBankRoleDTO())
                .build();
    }

    private RoleDTO defaultRoleDTO() {
        return RoleDTO.builder()
                .name("USER")
                .description("User role for test")
                .build();
    }

    private RoleDTO defaultAdminRoleDTO() {
        return RoleDTO.builder()
                .name("ADMIN")
                .description("Admin role for test")
                .build();
    }

    private RoleDTO defaultBankRoleDTO() {
        return RoleDTO.builder()
                .name("BANK_USER")
                .description("Bank role for test")
                .build();
    }

    private RoleDTO nullRole() {
        return RoleDTO.builder()
                .name("")
                .description("")
                .build();
    }
}
