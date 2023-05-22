package com.edu.icesi.TallerJPA.integration;

import com.edu.icesi.TallerJPA.TestConfigurationData;
import com.edu.icesi.TallerJPA.dto.LoginDTO;
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

    @Nested
    public class testsForCreateUserHappyPath{

        @Test
        public void testCreateUserFromAdmin() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(UserCreateDTO.builder()
                                            .firstName("Pepito")
                                            .lastName("Perez")
                                            .email("pepito@email.com")
                                            .phoneNumber("3177778942")
                                            .password("password")
                                            .icesiRole("ADMIN")
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            UserCreateDTO user = objectMapper.readValue(newResult.getResponse().getContentAsString(), UserCreateDTO.class);
            assertEquals(user.getEmail(),"pepito@email.com");
        }

        @Test
        public void testCreateUserFromBank() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(UserCreateDTO.builder()
                                            .firstName("Pepito")
                                            .lastName("Perez")
                                            .email("pepito2@email.com")
                                            .phoneNumber("3174545942")
                                            .password("password")
                                            .icesiRole("USER")
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            UserCreateDTO user = objectMapper.readValue(newResult.getResponse().getContentAsString(), UserCreateDTO.class);
            assertEquals(user.getEmail(),"pepito2@email.com");
        }

        @Test
        public void testCreateUserFromUser() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(UserCreateDTO.builder()
                                            .firstName("Pepito")
                                            .lastName("Perez")
                                            .email("pepito3@email.com")
                                            .phoneNumber("3274545942")
                                            .password("password")
                                            .icesiRole("USER")
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
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
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(UserCreateDTO.builder()
                                            .firstName("Pepe")
                                            .lastName("Perez")
                                            .email("pepe@email.com")
                                            .phoneNumber("3177878942")
                                            .password("password")
                                            .icesiRole("ADMIN")
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }

        @Test
        public void testCreateAdminWithBank() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/users/create/").content(
                                    objectMapper.writeValueAsString(UserCreateDTO.builder()
                                            .firstName("Pepe")
                                            .lastName("Perez")
                                            .email("pepe2@email.com")
                                            .phoneNumber("3178545942")
                                            .password("password")
                                            .icesiRole("ADMIN")
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            assertEquals(newResult.getResponse().getStatus(), 401);
        }
    }

}