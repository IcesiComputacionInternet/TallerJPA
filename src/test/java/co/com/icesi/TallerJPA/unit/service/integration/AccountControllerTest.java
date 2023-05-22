package co.com.icesi.TallerJPA.unit.service.integration;

import co.com.icesi.TallerJPA.TestConfigurationData;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiTransactionDTO;
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
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testTransferMoney() throws Exception{
        var resultTransfer = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultTransfer.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        IcesiTransactionDTO.builder()
                                                .accountNumberOrigin("799-948879-27")
                                                .accountNumberDestination("452-976314-32")
                                                .amount(150L)
                                                .messageResult("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testTransferMoneyWithDepositAccount() throws Exception{
        var resultTransfer = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultTransfer.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        IcesiTransactionDTO.builder()
                                                .accountNumberOrigin("799-948879-59")
                                                .accountNumberDestination("203-976314-32")
                                                .amount(150L)
                                                .messageResult("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testTransferMoneyWith0Amount() throws Exception{
        var resultTransfer = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultTransfer.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        IcesiTransactionDTO.builder()
                                                .accountNumberOrigin("799-948879-59")
                                                .accountNumberDestination("203-976314-32")
                                                .amount(0L)
                                                .messageResult("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testTransferMoneyWithOneOfTheNonExistAccount() throws Exception{
        var resultTransfer = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TokenDTO token = objectMapper.readValue(resultTransfer.getResponse().getContentAsString(), TokenDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        IcesiTransactionDTO.builder()
                                                .accountNumberOrigin("799-948879-59")
                                                .accountNumberDestination("98974629")
                                                .amount(100L)
                                                .messageResult("")
                                                .build()
                                ))
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
