package com.icesi.TallerJPA.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesi.TallerJPA.dto.request.IcesiLoginDTO;
import com.icesi.TallerJPA.dto.request.IcesiUserDTO;
import org.junit.jupiter.api.*;
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
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static String token = "";

    @Test
    void contextLoads() {
    }



    @Test
    @Order(1)
    public void testCreateUserEndPointWhenUserIsNotAuth() throws Exception {
        var result = mockMvc.perform(post("/user").content(
                        objectMapper.writeValueAsString(
                                IcesiUserDTO.builder()
                                        .email("test@gmail.com")
                                        .phoneNumber("+573191111111")
                                        .firstName("John")
                                        .lastName("Doe")
                                        .password("password")
                                        .rolName("ADMIN")
                                        .build()
                        )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    public void testCreateUserEndPointWhenUserAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("123456789@gmail.com")
                                                .phoneNumber("+573197419033")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
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
    public void testCreateUserEndPointWhenUserAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("123454489@gmail.com")
                                                .phoneNumber("+573197229033")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
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
    public void testCreateUserEndPointWhenUserAuthUser() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("user@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("123454489@gmail.com")
                                                .phoneNumber("+573197229033")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    public void testValidationEmailOrPhoneCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("")
                                                .phoneNumber("")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    public void testValidationEmailCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("NoEmail")
                                                .phoneNumber("+573226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    public void testValidationColombiaPhoneNumberCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("email@email.com")
                                                .phoneNumber("3226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    public void testValidationEmailOrPhoneCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("")
                                                .phoneNumber("")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    public void testValidationEmailCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("NoEmail")
                                                .phoneNumber("+573226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    public void testValidationColombiaPhoneNumberCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(post("/user").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserDTO.builder()
                                                .email("email@email.com")
                                                .phoneNumber("3226227443")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .password("password")
                                                .rolName("USER")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
