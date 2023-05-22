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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class RoleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    public class testsForCreateUserHappyPath{

        @Test
        public void testCreateRoleFromAdmin() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                    objectMapper.writeValueAsString(RoleCreateDTO.builder()
                                            .name("Example")
                                            .description("Role for demo")
                                            .icesiUsers(null)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            RoleCreateDTO role = objectMapper.readValue(newResult.getResponse().getContentAsString(), RoleCreateDTO.class);
            assertEquals(role.getName(),"Example");
        }
    }

    @Nested
    public class testForCreateUserNotHappyPath{

        @Test
        public void testCreateRoleFromUser() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                    objectMapper.writeValueAsString(RoleCreateDTO.builder()
                                            .name("Example")
                                            .description("Role for demo")
                                            .icesiUsers(null)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andReturn();

            assertEquals(403, newResult.getResponse().getStatus());
        }

        @Test
        public void testCreateRoleFromBank() throws Exception{
            var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password"))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                    objectMapper.writeValueAsString(RoleCreateDTO.builder()
                                            .name("Example")
                                            .description("Role for demo")
                                            .icesiUsers(null)
                                            .build())
                            )
                            .header("Authorization","Bearer "+token.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andReturn();

            assertEquals(403, newResult.getResponse().getStatus());
        }
    }

}