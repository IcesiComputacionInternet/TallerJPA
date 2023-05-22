package co.edu.icesi.tallerjpa.intefrations_test;

import co.edu.icesi.tallerjpa.dto.IcesiLoginDTO;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class AccountControllertest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IcesiUserRepository userRepository;

    private static String token = "";

    @Test
    void contextLoads() {
    }

    @Test
    @Order(1)
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
        var result = mockMvc.perform(
                        MockMvcRequestBuilders.patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "1", "2")
                                .content(objectMapper.writeValueAsString("100"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    public void testTransferMoneyEndPointWhenAuthUser() throws Exception {
        var resultToken = mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(new IcesiLoginDTO("user@email.com", "password")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();

        var result = mockMvc.perform(
                        MockMvcRequestBuilders.patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                                .content("1")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    public void testTransferMoneyEndPointWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(
                        MockMvcRequestBuilders.patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                                .content("1")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    public void testTransferMoneyEndPointWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(
                        MockMvcRequestBuilders.post("/login")
                                .content(objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password")))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(
                        MockMvcRequestBuilders.patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                                .content("1")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}

