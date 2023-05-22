package com.Icesi.TallerJPA.integration;

import com.Icesi.TallerJPA.TestConfigurationData;
import com.Icesi.TallerJPA.dto.IcesiUserDTO;
import com.Icesi.TallerJPA.dto.LoginDTO;
import com.Icesi.TallerJPA.dto.TokenDTO;
import com.Icesi.TallerJPA.error.exception.IcesiError;
import com.Icesi.TallerJPA.model.IcesiAccount;
import com.Icesi.TallerJPA.model.IcesiRole;
import com.Icesi.TallerJPA.model.IcesiUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.UUID;

import static com.Icesi.TallerJPA.api.UserAPI.BASE_USER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles="test")
@Import(TestConfigurationData.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public TokenDTO tokenEndpoint(String email, String password) throws Exception{
        var result= mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO(email,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
    }

    private TokenDTO tokenAdmin()throws Exception{
        return tokenEndpoint("johndoe@email.com","password");
    }

    private TokenDTO tokenUser()throws Exception{
        return tokenEndpoint("johndoe2@email.com","password");
    }

    private TokenDTO tokenBank()throws Exception{
        return tokenEndpoint("ethan@email.com","password");
    }




    @Test
    public void testCreateUserWithAdmin() throws Exception{

        TokenDTO tokenDTO=tokenAdmin();

        try {
            var result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL)
                            .content(objectMapper.writeValueAsString(defaultUserCreateDTO()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                    .andExpect(status().isOk())
                    .andReturn();
            IcesiUserDTO userDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserDTO.class);
            assertNotNull(userDTO);
        } catch (Exception e) {
            System.err.println(" ");
        }


    }


    @Test
    public void testCreateUserWhenEmailExists() throws Exception{

        TokenDTO tokenDTO = tokenAdmin();
        IcesiUser userDefault = defaultUserCreateDTO();
        userDefault.setPhoneNumber("185426235");

        try {
            var result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL)
                            .content(objectMapper.writeValueAsString(userDefault))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                    .andExpect(status().isConflict())
                    .andReturn();
            IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
            assertNotNull(icesiError);
            assertEquals("Email already exists", icesiError.getCode().getMessage());
        } catch (Exception e) {

            System.err.println(" ");
        }


    }


    @Test
    public void testCreateUserWhenPhoneNumberExists() throws Exception {

        TokenDTO tokenDTO = tokenAdmin();
        IcesiUser userDefault = defaultUserCreateDTO();


        try {
            var result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_USER_URL)
                            .content(objectMapper.writeValueAsString(userDefault))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken()))
                    .andExpect(status().isConflict())
                    .andReturn();
            IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
            assertNotNull(icesiError);
            assertEquals("Email already exists", icesiError.getCode().getMessage());
        } catch (Exception e) {

            System.err.println(" ");
        }

    }



    private IcesiUser defaultUserCreateDTO(){

        return IcesiUser.builder()
                .email("johndoe@email.com")
                .firstName("John")
                .icesiRole(icesiRole1())
                .lastName("Doe")
                .password("password")
                .phoneNumber("12313423153")
                .userId(UUID.randomUUID())
                .build();
    }
        private IcesiUser defaultAdminUserCreateDTO(){

            return IcesiUser.builder()
                    .email("johndoe2@email.com")
                    .firstName("John")
                    .icesiRole(icesiRole2())
                    .lastName("Doe")
                    .password("password")
                    .phoneNumber("1234523123")
                    .userId(UUID.randomUUID())
                    .build();
        }

        private IcesiUser defaultBankUserCreateDTO(){

            return IcesiUser.builder()
                    .userId(UUID.randomUUID())
                    .email("ethan@email.com")
                    .icesiRole(icesiRole3())
                    .firstName("Ethan")
                    .lastName("Torch")
                    .phoneNumber("32014354789")
                    .password("password")
                    .build();
        }

    private IcesiRole icesiRole2() {
        return  IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("USER")
                .build();
    }
    private IcesiRole icesiRole1() {
        return  IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("ADMIN")
                .build();
    }
    private IcesiRole icesiRole3() {
        return  IcesiRole.builder()
                .roleId(UUID.randomUUID())
                .description("Role for demo")
                .name("BANK")
                .build();
    }
}
