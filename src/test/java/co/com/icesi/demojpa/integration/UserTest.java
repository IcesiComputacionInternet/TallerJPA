package co.com.icesi.demojpa.integration;

import co.com.icesi.demojpa.TestConfigurationData;
import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TokenDTO;
import co.com.icesi.demojpa.dto.TransactionDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.error.exception.IcesiError;
import co.com.icesi.demojpa.error.exception.IcesiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class UserTest{

    @Autowired
    private MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;


    //LOG IN

    @Test
    public void testTokenEndpointHappyPath() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(result);
    }

    @Test
    public void testTokenEndpointInvalidUser() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email4.com","password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals("Bad credentials", error.getDetails());
        assertNotNull(error);
    }

    @Test
    public void testTokenEndpointPassword() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","NotApassword"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertNotNull(error);
        assertEquals("Bad credentials", error.getDetails());
    }

    //CREATE USER
    @Test
    public void testCreateAdminUserEndpointHappyPath() throws Exception {
        token =mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
                                objectMapper.writeValueAsString(new UserCreateDTO(
                                        "nombre",
                                        "apellido",
                                        "123@gmail.co",
                                        "+57 305 123 432",
                                        "123",
                                        "ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk());
        ResponseUserDTO user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ResponseUserDTO.class);
        assertNotNull(user);
    }

    @Test
    public void testCreateUserEndpointHappyPath() throws Exception {
        token =mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
                                objectMapper.writeValueAsString(new UserCreateDTO(
                                        "nombre",
                                        "apellido",
                                        "123456@gmail.co",
                                        "+57 305 123 555",
                                        "123",
                                        "USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isOk());
        ResponseUserDTO user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ResponseUserDTO.class);
        assertNotNull(user);
    }


    @Test
    public void testCreateAdminUserNotValidEndpoint() throws Exception {
        token =mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
                                objectMapper.writeValueAsString(new UserCreateDTO(
                                        "nombre",
                                        "apellido",
                                        "1236@gmail.co",
                                        "+57305123432",
                                        "123",
                                        "ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().is5xxServerError());
        IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
        assertEquals("Solo un usuario con rol ADMIN puede crear un usuario con rol ADMIN", error.getDetails());
    }


    @Test
    public void testCreateUserEndpointInvalidRole() throws Exception {
        token=mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
                                objectMapper.writeValueAsString(new UserCreateDTO(
                                        "nombre",
                                        "apellido",
                                        "123@gmail.co",
                                        "+57 305 123 432",
                                        "123",
                                        "a")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().is4xxClientError());
        IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
        assertEquals("ROLE_NOT_FOUND", error.getDetails());
    }

    @Test
    public void testCreateUserEndpointInvalidGmail() throws Exception {
        token=mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
                                objectMapper.writeValueAsString(new UserCreateDTO(
                                        "nombre",
                                        "apellido",
                                        "123gmail",
                                        "+57 305 123 432",
                                        "123",
                                        "USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserEndpointInvalidPhoneNumber() throws Exception {
        token=mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TokenDTO tokenDTO = objectMapper.readValue(token, TokenDTO.class);
        mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
                                objectMapper.writeValueAsString(new UserCreateDTO(
                                        "nombre",
                                        "apellido",
                                        "123@gmail.co",
                                        "+34 305 123 432",
                                        "123",
                                        "USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken()))
                .andExpect(status().isBadRequest());
    }
}

