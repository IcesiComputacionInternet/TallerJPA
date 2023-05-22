package co.com.icesi.TallerJPA.unit.service.integration;

import co.com.icesi.TallerJPA.TestConfigurationData;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.LoginDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.TokenDTO;
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
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateAnUserByAnAdmin() throws Exception{
        var resultCreate = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe1@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultCreate.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserCreateDTO.builder()
                                                .firstName("Martina")
                                                .lastName("Loaiza")
                                                .email("sibuenas@hotmail.com")
                                                .phoneNumber("+57123123123")
                                                .password("password")
                                                .role(defaultRoleDTO())
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateAnUserByABank() throws Exception{
        var resultCreate = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultCreate.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserCreateDTO.builder()
                                                .firstName("Martina")
                                                .lastName("Loaiza")
                                                .email("sibuenas2@hotmail.com")
                                                .phoneNumber("+57143143123")
                                                .password("password")
                                                .role(defaultRoleDTO2())
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateAnUserByAnUser() throws Exception{
        var resultCreate = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultCreate.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserCreateDTO.builder()
                                                .firstName("Martina")
                                                .lastName("Loaiza")
                                                .email("sibuenas@hotmail.com")
                                                .phoneNumber("+57153123153")
                                                .password("password")
                                                .role(defaultRoleDTO())
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateAnAdminByAnUser() throws Exception{
        var resultCreate = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe5@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultCreate.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserCreateDTO.builder()
                                                .firstName("Martina")
                                                .lastName("Loaiza")
                                                .email("sibuenas@hotmail.com")
                                                .phoneNumber("+57123658123")
                                                .password("password")
                                                .role(defaultRoleDTO())
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void testCreateAnAdminByABank() throws Exception{
        var resultCreate = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe6@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultCreate.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
                                objectMapper.writeValueAsString(
                                        IcesiUserCreateDTO.builder()
                                                .firstName("Martina")
                                                .lastName("Loaiza")
                                                .email("sibuenas@hotmail.com")
                                                .phoneNumber("+57123658123")
                                                .password("password")
                                                .role(defaultRoleDTO())
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private IcesiRoleCreateDTO defaultRoleDTO(){
        return   IcesiRoleCreateDTO.builder()
                .name("ADMIN")
                .description("El administrador se encarga de administrar la página")
                .build();
    }

    private IcesiRoleCreateDTO defaultRoleDTO2(){
        return   IcesiRoleCreateDTO.builder()
                .name("USER")
                .description("El usuario se encarga de usar la página")
                .build();
    }
}
