package co.com.icesi.TallerJPA.integration;


import co.com.icesi.TallerJPA.dto.IcesiAccountDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.dto.LoginDto;
import co.com.icesi.TallerJPA.dto.TransactionRequestDTO;
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
@Import(TestConfigurationData.class )
@ActiveProfiles(profiles = "test")
@SpringBootTest
public class IcesiAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    private String token = "";

    private static final String URL= "/account/transferMoney";



     @Test
     public void createAccount() throws Exception{
         setToken("admin@email.com");
         var  result = mockMvc.perform(MockMvcRequestBuilders.post("/account").content(
                                 objectMapper.writeValueAsString(IcesiAccountDTO.builder()
                                         .balance(100L)
                                         .type("AHORROS")
                                         .user(IcesiUserDTO.builder()
                                                 .email("admin@email.com").build()).build())
                                          )
                         .header("Authorization", "Bearer " + token)
                         .contentType(MediaType.APPLICATION_JSON)
                         .accept(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andReturn();

         System.out.println(result.getResponse().getContentAsString());

     }

    @Test
    public void createAccountWithBlankType() throws Exception{
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post("/account").content(
                                objectMapper.writeValueAsString(IcesiAccountDTO.builder()
                                        .balance(100L)
                                        .type("")
                                        .user(IcesiUserDTO.builder()
                                                .email("admin@email.com").build()).build())
                        )
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void transferMoneyWhenNoUserAuth() throws Exception {
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL).content(
                                objectMapper.writeValueAsString(defaultTransaction()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void transferMoneyWhenUserAuth() throws Exception {
        setToken("user@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL).content(
                                objectMapper.writeValueAsString(defaultTransaction()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void transferMoneyWhenAdminAuth() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL).content(
                                objectMapper.writeValueAsString(defaultTransaction()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void transferMoneyWhenBankAuth() throws Exception {
        setToken("bank@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL).content(
                                objectMapper.writeValueAsString(defaultTransaction()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void transferMoneyWhenTheAccountIsDeposit() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL).content(
                                objectMapper.writeValueAsString(defaultTransactionDesposit()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    public void transferMoneyWhenTheBalanceToTransferIsLessThanZero() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.patch(URL).content(
                                objectMapper.writeValueAsString(TransactionRequestDTO.builder()
                                        .accountNumberFrom("956-648065-61")
                                        .accountNumberTo("903-178442-08")
                                        .amount(-1L)
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }




    public void  setToken(String email) throws Exception {
        var login =   mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").content(
                                objectMapper.writeValueAsString(LoginDto.builder().userName(email).password("password").build())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var response = objectMapper.readTree(login.getResponse().getContentAsString());
        token = response.get("token").asText();
        System.out.println("Actual token: "+ token);
    }

    private TransactionRequestDTO defaultTransaction(){
        return TransactionRequestDTO.builder()
                .accountNumberFrom("956-648065-61")
                .accountNumberTo("903-178442-08")
                .amount(100L)
                .build();
    }

    private TransactionRequestDTO defaultTransactionDesposit() {
        return TransactionRequestDTO.builder()
                .accountNumberFrom("903-178442-08")
                .accountNumberTo("903-178442-62")
                .amount(100L)
                .build();
    }


}
