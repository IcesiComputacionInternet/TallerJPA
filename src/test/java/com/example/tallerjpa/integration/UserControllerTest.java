package com.example.tallerjpa.integration;


import com.example.tallerjpa.dto.LoginDTO;
import com.example.tallerjpa.dto.RoleDTO;
import com.example.tallerjpa.dto.TokenDTO;
import com.example.tallerjpa.dto.UserDTO;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Import(TestConfigurationData.class )
@ActiveProfiles(profiles = "test")
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    private String token = "";

    private static final String URL= "/users";

    @Test
    public void testCreateUserNoAuth() throws Exception {
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWithAuth() throws Exception {
        setToken();
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWithInvalidNumber() throws Exception {
        setToken();
        UserDTO user = defaultUser();
        user.setPhoneNumber("+1342993845");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWithInvalidEmail() throws Exception {
        setToken();
        UserDTO user = defaultUser();
        user.setEmail("test.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }





    public void  setToken() throws Exception {
        var login =   mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(LoginDTO.builder().username("juan@hotmail.com").password("password").build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO tokenDTO = objectMapper.readValue(login.getResponse().getContentAsString(), TokenDTO.class);
        token = tokenDTO.getToken();
    }

    private UserDTO defaultUser(){
        return UserDTO.builder()
                .firstName("Juan")
                .lastName("Osorio")
                .email("juanosorio@hotmail.com")
                .phoneNumber("+573007896543")
                .password("password")
                .role(RoleDTO.builder()
                        .name("ADMIN")
                        .description("Role for admin's")
                        .build())
                .build();
    }
}
