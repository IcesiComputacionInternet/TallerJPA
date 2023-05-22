package com.example.jpa.integration;

import com.example.jpa.TestConfigurationData;
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
    public void testCreateUserEndpoint() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    // Create a integration test for the endpoint /users/all
    // The test should return a list of users
    // The test should return a status 200
    @Test
    public void testGetAllUsersEndpoint() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    //Create an integration test fot the endpoint /users/{userId}/
    //The test should return a user
    //The test should return a status 200
    @Test
    public void testGetUserEndpoint() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthAdminToCreateUser() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(defaultUserDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthAdminToCreateBank() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthAdminToCreateAdmin() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthUserToCreateUser() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthUserToCreateBank() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthUserToCreateAdmin() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthBankToCreateUser() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(userByBankCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthBankToCreateAdmin() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(defaultAdminDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthBankToCreateBank() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(bankByBankCreateDTO())
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
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutFirtsName() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutLastName() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
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
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithEmailBadFormat() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }


    @Test   //This test is to fail the input validation of the @ColombianNumberConstraint phoneNumber
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithPhoneBadFormat() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @NotBlank password
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutPassword() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @NotBlank role
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutRole() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/createUser").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
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
                .phoneNumber("+573162499422")
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
                .name("BANK")
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
