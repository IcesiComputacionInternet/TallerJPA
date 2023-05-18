package co.com.icesi.tallerjpa.integration;

import co.com.icesi.tallerjpa.dto.LoginDTO;
import co.com.icesi.tallerjpa.dto.RequestUserDTO;
import co.com.icesi.tallerjpa.dto.TransactionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class TallerJPATest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    private static String key = "";

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class AuthControllerTest {
        @Test
        @Order(1)
        public void contextLoads() {
        }

        @Test
        @Order(2)
        public void testLoginEndPoint() throws Exception {
            var result = mvc.perform(MockMvcRequestBuilders.post("/login").content(
                            mapper.writeValueAsString(
                                    new LoginDTO("admin@email.com", "password")
                            )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();

            key = result.getResponse().getContentAsString();

            System.out.println(result.getResponse().getContentAsString());

        }
    }

    @Nested
    @Order(2)
    public class UserControllerTest {
        @Test
        public void testCreateUserEndPointWhenUserIsNotAuth() throws Exception {
            var result = mvc.perform(post("/users").content(
                            mapper.writeValueAsString(
                                    RequestUserDTO.builder()
                                            .email("123456789@gmail.com")
                                            .phoneNumber("+573197419033")
                                            .firstName("John")
                                            .lastName("Doe")
                                            .password("password")
                                            .role("ADMIN")
                                            .build()
                            )).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            System.out.println(result.getResponse().getContentAsString());
        }

        @Test
        public void testCreateUserEndPointWhenUserIsAuth() throws Exception {
            var result = mvc.perform(post("/users")
                            .content(mapper.writeValueAsString(
                                    RequestUserDTO.builder()
                                            .email("123456789@gmail.com")
                                            .phoneNumber("+573197419033")
                                            .firstName("John")
                                            .lastName("Doe")
                                            .password("password")
                                            .role("ADMIN")
                                            .build()
                            ))
                            .header("Authorization", "Bearer " + key)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            System.out.println(result.getResponse().getContentAsString());
        }
    }

    @Nested
    @Order(3)
    public class accountControllerTest {
        @Test
        public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
            var result = mvc.perform(post("/accounts/transfer")
                            .content(mapper.writeValueAsString(
                                    TransactionDTO.builder()
                                            .amount(1000L)
                                            .accountNumberOrigin("123456789")
                                            .accountNumberDestination("987654321")
                                            .build()
                            ))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isUnauthorized())
                    .andReturn();

            System.out.println(result.getResponse().getContentAsString());
        }

        @Test
        public void testTransferMoneyEndPointWhenUserIsAuth() throws Exception {
            var result = mvc.perform(post("/accounts/transfer")
                            .content(mapper.writeValueAsString(
                                    TransactionDTO.builder()
                                            .amount(1000L)
                                            .accountNumberOrigin("123456789")
                                            .accountNumberDestination("987654321")
                                            .build()
                            ))
                            .header("Authorization", "Bearer " + key)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andReturn();

            System.out.println(result.getResponse().getContentAsString());
        }
    }
}
