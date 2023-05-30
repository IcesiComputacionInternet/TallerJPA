package com.edu.icesi.TallerJPA.integration;

import com.edu.icesi.TallerJPA.TestConfigurationData;
import com.edu.icesi.TallerJPA.dto.LoginDTO;
import com.edu.icesi.TallerJPA.dto.IcesiRoleDTO;
import com.edu.icesi.TallerJPA.dto.TokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleControllerTests {

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


        @Test
        public void testCreateRoleAdmin() throws Exception{

            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                    objectMapper.writeValueAsString(defaultRole())
                            )
                            .header("Authorization","Bearer "+generateAdminToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            IcesiRoleDTO role = objectMapper.readValue(newResult.getResponse().getContentAsString(), IcesiRoleDTO.class);
            assertEquals(role.getName(),"Example");
        }

    @Test
    public void testCreateRoleBank() throws Exception{

        var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                objectMapper.writeValueAsString(defaultRole())
                        )
                        .header("Authorization","Bearer "+generateBankToken().getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        assertEquals(403, newResult.getResponse().getStatus());
    }




        @Test
        public void testCreateRoleUser() throws Exception{
            var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                    objectMapper.writeValueAsString(defaultRole())
                            )
                            .header("Authorization","Bearer "+generateUserToken().getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andReturn();

            assertEquals(403, newResult.getResponse().getStatus());
        }


    @Test
    public void testCreateRoleAlreadyExists() throws Exception{

        IcesiRoleDTO role = defaultRole();
        role.setName("ADMIN");

        var newResult = mockMvc.perform(MockMvcRequestBuilders.post("/roles/create/").content(
                                objectMapper.writeValueAsString(role)
                        )
                        .header("Authorization","Bearer "+generateAdminToken().getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();

        assertEquals(409, newResult.getResponse().getStatus());
    }

    public IcesiRoleDTO defaultRole(){
        return IcesiRoleDTO.builder()
                .name("Example")
                .description("Role for demo")
                .icesiUsers(null)
                .build();
    }

}