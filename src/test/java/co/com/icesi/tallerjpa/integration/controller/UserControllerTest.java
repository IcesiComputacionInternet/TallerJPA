package co.com.icesi.tallerjpa.integration.controller;

import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.integration.configuration.TestConfigurationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Value("${security.token.permanent}")
    private String key;

    @Test
    public void testCreateUserWhenUserIsNotAuth() throws Exception {
        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(defaultUser()))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenUserIsAuth() throws Exception {
        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenEmailIsInvalid() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setEmail("12345678910gmail.com");

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenPhoneNumberIsNotColombian() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setPhoneNumber("12345678910");

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenRoleIsNull() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setRole(null);

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenFirstNameIsBlank() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setFirstName("");

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenLastNameIsBlank() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setLastName("");

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenPasswordIsBlank() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setPassword("");

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenEmailAndPhoneNumberAreBlank() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setEmail("");
        user.setPhoneNumber("");

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenEmailIsNull() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setEmail(null);

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenPhoneNumberIsNull() throws Exception {
        RequestUserDTO user = defaultUser();
        user.setPhoneNumber(null);

        var result = mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .header("Authorization", "Bearer " + key)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    private RequestUserDTO defaultUser() {
        return RequestUserDTO.builder()
                .email("12345678910@gmail.com")
                .phoneNumber("+573197419034")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .role("ADMIN")
                .build();
    }
}
