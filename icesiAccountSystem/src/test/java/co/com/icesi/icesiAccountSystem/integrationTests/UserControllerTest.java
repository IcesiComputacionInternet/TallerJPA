package co.com.icesi.icesiAccountSystem.integrationTests;

import co.com.icesi.icesiAccountSystem.dto.LoginDTO;
import co.com.icesi.icesiAccountSystem.dto.RequestUserDTO;
import co.com.icesi.icesiAccountSystem.dto.TokenDTO;
import co.com.icesi.icesiAccountSystem.error.exception.AccountSystemError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles="test")

class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }
    @Test
    public void testCreateAUserWhenRoleNameIsNull() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        RequestUserDTO.builder()
                                                .email("sara@gmail.com")
                                                .phoneNumber("+573254789615")
                                                .firstName("Sara")
                                                .lastName("Alvarez")
                                                .password("password")
                                                .roleName(null)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAdminUserWhenUserLoggedIsABankUser() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("ethan@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        defaultUserDTO().setRoleName("ADMIN");
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        defaultUserDTO()
                                ))
                        .header("Authorization",  "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testCreateAUserWhenUserLoggedIsABankUser() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("ethan@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        defaultUserDTO()
                                ))
                        .header("Authorization",  "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testCreateAUserWhenUserLoggedIsANormalUser() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        defaultUserDTO()
                                ))
                        .header("Authorization",  "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testCreateAUserWhenLoggedUserIsAdminHappyPath() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        defaultUserDTO()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAUserWithBlankRole() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        RequestUserDTO.builder()
                                                .email("sara@gmail.com")
                                                .phoneNumber("+573254789615")
                                                .firstName("Sara")
                                                .lastName("Alvarez")
                                                .password("password")
                                                .roleName("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        AccountSystemError error =objectMapper.readValue(result.getResponse().getContentAsString(),AccountSystemError.class);
        assertNotNull(error);
        var details = error.getDetails();
        assertEquals(1, details.size());
        var detail = details.get(0);
        assertEquals("Role with name:  not found.",detail.getErrorMessage());
    }

    private RequestUserDTO defaultUserDTO(){
        return RequestUserDTO.builder()
                .email("damiano@gmail.com")
                .phoneNumber("+573197419033")
                .firstName("Damiano")
                .lastName("David")
                .password("password")
                .roleName("USER")
                .build();
    }

}
