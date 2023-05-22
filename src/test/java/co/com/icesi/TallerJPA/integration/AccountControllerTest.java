package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.TransactionOperationDTO;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.integration.config.TestConfigurationData;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class AccountControllerTest {
    @Autowired
    private MockMvc mocMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${security.token.admin}")
    private String tokenAdmin;
    @Value("${security.token.user}")
    private String tokenUser;
    @Value("${security.token.bank}")
    private String tokenBank;

    @Test
    void contextLoads() {

    }
    @Test
    public void testAccountsEndpointWhenUserNotAuth() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts").content(
                                objectMapper.writeValueAsString(defaultAdminCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyEndPointWhenUserIsNotAuth() throws Exception {
        var result = mocMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(1000L)
                                        .accountFrom("123456789")
                                        .accountTo("987654321")
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
        var result = mocMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(1000L)
                                        .accountFrom("123456789")
                                        .accountTo("987654321")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    //Test to fail the input validations of TransactionOperationDTO
    @Test
    public void testTransactionDTOWhenAmountIsNegative() throws Exception {
        var result = mocMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(-1000L)
                                        .accountFrom("123456789")
                                        .accountTo("987654321")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransactionDTOWhenAmountIsBlank() throws Exception {
        var result = mocMvc.perform(patch("/accounts/transfer")
                        .content(objectMapper.writeValueAsString(
                                TransactionOperationDTO.builder()
                                        .amount(null)
                                        .accountFrom("123456789")
                                        .accountTo("987654321")
                                        .build()
                        ))
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    private UserCreateDTO defaultAdminCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Andres")
                .lastName("Gomez")
                .email("test@hotmail.com")
                .phoneNumber("+573154620529")
                .password("1234")
                .role("ADMIN")
                .build();
    }

}
