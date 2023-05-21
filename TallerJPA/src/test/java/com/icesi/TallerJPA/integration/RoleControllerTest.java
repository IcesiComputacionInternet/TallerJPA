package com.icesi.TallerJPA.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesi.TallerJPA.dto.request.IcesiLoginDTO;
import com.icesi.TallerJPA.dto.request.IcesiRoleDTO;
import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import com.icesi.TallerJPA.repository.UserRespository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRespository userRespository;

    private static String token = "";

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testCreateRoleWhenIsNotAuth() throws Exception {
        var result = mockMvc.perform(post("/role").content(
                        objectMapper.writeValueAsString(
                                IcesiRoleDTO.builder()
                                        .description("Role for demo")
                                        .name("NEW_ROLE")
                                        .build()
                        )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    public void testCreateRoleWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/role").content(
                                objectMapper.writeValueAsString(
                                        IcesiRoleDTO.builder()
                                                .description("Role for demo")
                                                .name("NEW_ROLE")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    public void testValidationEmptyDescriptionWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/role").content(
                                objectMapper.writeValueAsString(
                                        IcesiRoleDTO.builder()
                                                .description("")
                                                .name("NEW_ROLE")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    public void testValidationEmptyNameWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/role").content(
                                objectMapper.writeValueAsString(
                                        IcesiRoleDTO.builder()
                                                .description("Role for demo")
                                                .name("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
