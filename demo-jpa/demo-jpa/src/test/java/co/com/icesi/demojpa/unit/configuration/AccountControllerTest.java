package co.com.icesi.demojpa.unit.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesi.TallerJPA.dto.request.IcesiAccountDTO;
import com.icesi.TallerJPA.dto.request.IcesiLoginDTO;
import com.icesi.TallerJPA.enums.IcesiAccountType;
import com.icesi.TallerJPA.repository.UserRespository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRespository userRespository;

    private static String token = "";

    @Test
    void contextLoads() {
    }


    @Test
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
        var result = mockMvc.perform(
                        patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "1", "2")
                                .content(objectMapper.writeValueAsString("100"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenAuthUser() throws Exception {
        var resultToken = mockMvc.perform(post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("user@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();

        var result = mockMvc.perform(patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                        .content("1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                        .content("1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();
        var result = mockMvc.perform(patch("/account/transfer/{accountNumberOrigin}/{accountNumberDestination}", "123", "456")
                        .content("1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenIsNotAuth() throws Exception {
        var result = mockMvc.perform(post("/account").content(
                                objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .emailUser("user@email.com")
                                                .active(true)
                                                .balance(50)
                                                .type(IcesiAccountType.DEFAULT)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenAuthUser() throws Exception {
        var resultToken = mockMvc.perform(post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("user@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();

        var result = mockMvc.perform(post("/account").content(
                                objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .emailUser("user@email.com")
                                                .active(true)
                                                .balance(50)
                                                .type(IcesiAccountType.DEFAULT)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenAuthAdmin() throws Exception {
        var resultToken = mockMvc.perform(post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("admin@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();

        var result = mockMvc.perform(post("/account").content(
                                objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .emailUser("user@email.com")
                                                .active(true)
                                                .balance(50)
                                                .type(IcesiAccountType.DEFAULT)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserWhenAuthBank() throws Exception {
        var resultToken = mockMvc.perform(post("/login").content(
                                objectMapper.writeValueAsString(new IcesiLoginDTO("bank@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        token = resultToken.getResponse().getContentAsString();

        var result = mockMvc.perform(post("/account").content(
                                objectMapper.writeValueAsString(
                                        IcesiAccountDTO.builder()
                                                .emailUser("user@email.com")
                                                .active(true)
                                                .balance(50)
                                                .type(IcesiAccountType.DEFAULT)
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
