package com.edu.icesi.TallerJPA.integration;

import com.edu.icesi.TallerJPA.TestConfigurationData;
import com.edu.icesi.TallerJPA.dto.LoginDTO;
import com.edu.icesi.TallerJPA.dto.RoleCreateDTO;
import com.edu.icesi.TallerJPA.dto.TokenDTO;
import com.edu.icesi.TallerJPA.dto.UserCreateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public TokenDTO generateAdminToken() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
    }

    public TokenDTO generateUserToken() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
    }

    public TokenDTO generateBankToken() throws Exception{
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
    }

    @Nested
    public class testsForCreateUserHappyPath{

        @Test
        public void testCreateUserFromAdmin() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(defaultAdminUser())
                            )
                            .header("Authorization","Bearer "+generateAdminToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            UserCreateDTO user = objectMapper.readValue(newResult.getResponse().getContentAsString(), UserCreateDTO.class);
            assertEquals(user.getEmail(),"pepito@email.com");
        }

        @Test
        public void testCreateUserFromBank() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(defaultUser1())
                            )
                            .header("Authorization","Bearer "+generateBankToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            UserCreateDTO user = objectMapper.readValue(newResult.getResponse().getContentAsString(), UserCreateDTO.class);
            assertEquals(user.getEmail(),"pepito2@email.com");
        }

        @Test
        public void testCreateUserFromUser() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(defaultUser2())
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            UserCreateDTO user = objectMapper.readValue(newResult.getResponse().getContentAsString(), UserCreateDTO.class);
            assertEquals(user.getEmail(),"pepito3@email.com");
        }
    }

    @Nested
    public class testForCreateUserNotHappyPath{

        @Test
        public void testCreateAdminWithUser() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(defaultAdminUser())
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }

        @Test
        public void testCreateAdminWithBank() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(defaultAdminUser())
                            )
                            .header("Authorization","Bearer "+generateBankToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }

        @Test
        public void testCreateBankWithUser() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(defaultBankUser())
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }

        @Test
        public void testCreateUserWithAdminButBadPhoneNumber() throws Exception{

            UserCreateDTO user = defaultUser1();
            user.setPhoneNumber("3145794414");

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(user)
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 400);
        }

        @Test
        public void testCreateUserThatEmailAndPhoneAlreadyExists() throws Exception{

            UserCreateDTO user = defaultUser1();
            user.setEmail("johndoe@email.com");

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(user)
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andReturn();

            assertEquals(409, newResult.getResponse().getStatus());
        }

        @Test
        public void testCreateUserThatEmailAlreadyExists() throws Exception{

            UserCreateDTO user = defaultUser1();
            user.setEmail("johndoe@email.com");
            user.setPhoneNumber("+3105446798");

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(user)
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, newResult.getResponse().getStatus());
        }

        @Test
        public void testCreateUserThatPhoneAlreadyExists() throws Exception{

            UserCreateDTO user = defaultUser1();
            user.setEmail("pepitope@email.com");
            user.setPhoneNumber("+57123123123");

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(user)
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            assertEquals(400, newResult.getResponse().getStatus());
        }
    }

    public UserCreateDTO defaultAdminUser(){
        return UserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepito@email.com")
                .phoneNumber("+573177778942")
                .password("password")
                .icesiRole("ADMIN")
                .build();
    }

    public UserCreateDTO defaultUser1(){
        return UserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepito2@email.com")
                .phoneNumber("+573174545942")
                .password("password")
                .icesiRole("USER")
                .build();
    }

    public UserCreateDTO defaultUser2(){
        return UserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepito3@email.com")
                .phoneNumber("+573274545942")
                .password("password")
                .icesiRole("USER")
                .build();
    }

    public UserCreateDTO defaultBankUser(){
        return UserCreateDTO.builder()
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepito3@email.com")
                .phoneNumber("+573274545942")
                .password("password")
                .icesiRole("BANK")
                .build();
    }

}