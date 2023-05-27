package com.edu.icesi.demojpa.integration.controller;

import com.edu.icesi.demojpa.dto.request.LoginDTO;
import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.dto.request.TokenDTO;
import com.edu.icesi.demojpa.integration.TestConfigurationData;
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
@SpringBootTest
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class UserApplicationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateUserByAdmin() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        RequestUserDTO.builder()
                                                .email("luismiguelossaarias05@gmail.com")
                                                .phoneNumber("+573174833968")
                                                .firstName("Luis Miguel")
                                                .lastName("Ossa Arias")
                                                .password("password")
                                                .role("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateUserByBank() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        RequestUserDTO.builder()
                                                .email("exmaample@gmail.com")
                                                .phoneNumber("+573174839968")
                                                .firstName("Lois")
                                                .lastName("Prank")
                                                .password("password")
                                                .role("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateAdminUserByBank() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        RequestUserDTO.builder()
                                                .email("luismiguelossaarias05@gmail.com")
                                                .phoneNumber("+573174833968")
                                                .firstName("Luis Miguel")
                                                .lastName("Ossa Arias")
                                                .password("password")
                                                .role("ADMIN")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testCreateUserByUser() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        RequestUserDTO.builder()
                                                .email("luismiguelossaarias05@gmail.com")
                                                .phoneNumber("+573174833968")
                                                .firstName("Luis Miguel")
                                                .lastName("Ossa Arias")
                                                .password("password")
                                                .role("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
