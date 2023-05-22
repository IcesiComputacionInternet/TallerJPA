package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.dto.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class )
@ActiveProfiles(profiles = "test")
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String token = "";

    private static final String URL = "/role";

    @BeforeEach
    public  void init() throws Exception {
        setToken();
    }


    @Test
    public void createRole() throws Exception{
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiRoleDTO.builder().
                                                name("ANOTHER:ROLE")
                                                .description("AnotherRole")
                                                .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void createRoleWithBlankName() throws Exception{
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiRoleDTO.builder().
                                        name("")
                                        .description("Blank name jaja")
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }







    public void  setToken() throws Exception {
        var login =   mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").content(
                                objectMapper.writeValueAsString(LoginDto.builder().userName("admin@email.com").password("password").build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var response = objectMapper.readTree(login.getResponse().getContentAsString());
        token = response.get("token").asText();
        System.out.println("Actual token: "+ token);
    }
}
