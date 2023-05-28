package co.com.icesi.icesiAccountSystem.integrationTests;

import co.com.icesi.icesiAccountSystem.dto.LoginDTO;
import co.com.icesi.icesiAccountSystem.dto.RequestAccountDTO;
import co.com.icesi.icesiAccountSystem.dto.TokenDTO;
import co.com.icesi.icesiAccountSystem.dto.TransactionOperationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles="test")

public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {

    }

    @Test
    public void testCreateAnAccountWhenLoggedUserIsAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/create").content(
                                objectMapper.writeValueAsString(
                                        RequestAccountDTO.builder()
                                                .userEmail("keren@email.com")
                                                .balance(50000000)
                                                .type("deposit only")
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateAnAccountWhenUserLoggedIsABankUser() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("ethan@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/create").content(
                                objectMapper.writeValueAsString(
                                        RequestAccountDTO.builder()
                                                .userEmail("keren@email.com")
                                                .balance(50000000)
                                                .type("deposit only")
                                                .build()
                                ))
                        .header("Authorization",  "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        int status = result.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Enable result: "+message);
    }

    @Test
    public void testAUserCanCreateAccountsByHimself() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/create").content(
                                objectMapper.writeValueAsString(
                                        RequestAccountDTO.builder()
                                                .userEmail("keren@email.com")
                                                .balance(1800000)
                                                .type("deposit only")
                                                .build()
                                ))
                        .header("Authorization",  "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testAUserCannotCreateAccountsForOtherUsers() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/create").content(
                                objectMapper.writeValueAsString(
                                        RequestAccountDTO.builder()
                                                .userEmail("ethan@email.com")
                                                .balance(1800000)
                                                .type("deposit only")
                                                .build()
                                ))
                        .header("Authorization",  "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        int status = result.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Enable result: "+message);
    }

    @Test
    public void testDisableAnAccountWhenLoggedUserIsAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/disable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testEnableAnAccountWhenLoggedUserIsAdmin() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result1=mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/disable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var result2= mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/enable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("Disable result: "+result1.getResponse().getContentAsString());
        System.out.println("Enable result: "+result2.getResponse().getContentAsString());
    }

    @Test
    public void testDisableAnAccountWhenLoggedUserIsNotOwner() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("ethan@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/disable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        int status = result.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Enable result: "+message);
    }

    @Test
    public void testEnableAnAccountWhenLoggedUserIsNotOwner() throws Exception {
        var resultToken1 = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token1 = objectMapper.readValue(resultToken1.getResponse().getContentAsString(),TokenDTO.class);
        var result1=mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/disable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token1.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var resultToken2 = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("ethan@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token2 = objectMapper.readValue(resultToken2.getResponse().getContentAsString(),TokenDTO.class);
        var result2 = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/enable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token2.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println("Disable result: "+result1.getResponse().getContentAsString());
        int status = result2.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Enable result: "+message);
    }

    @Test
    public void testTransferMoneyWhenAccountFromDoesNotExists() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-08")
                                                .accountTo("025-253568-33")
                                                .amount(100000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyWhenAccountToDoesNotExists() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-25")
                                                .accountTo("025-253568-09")
                                                .amount(100000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testTransferMoneyWhenAmountIsGreaterThanAccountsFromBalance() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-25")
                                                .accountTo("025-253568-33")
                                                .amount(80000000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        int status = result.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Transfer operation result: "+message);
    }

    @Test
    public void testTransferMoneyWhenAccountFromIsDisabled() throws Exception {
        var resultToken1 = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token1 = objectMapper.readValue(resultToken1.getResponse().getContentAsString(),TokenDTO.class);
        var result1=mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/disable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token1.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var resultToken2 = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token2 = objectMapper.readValue(resultToken2.getResponse().getContentAsString(),TokenDTO.class);
        var result2 = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-54")
                                                .accountTo("025-253568-33")
                                                .amount(1000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token2.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println("Disable account result: "+result1.getResponse().getContentAsString());
        int status = result2.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Transfer operation result: "+message);
    }

    @Test
    public void testTransferMoneyWhenAccountToIsDisabled() throws Exception {
        var resultToken1 = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token1 = objectMapper.readValue(resultToken1.getResponse().getContentAsString(),TokenDTO.class);
        var result1=mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/disable/{accountNumber}","025-253568-54")
                        .header("Authorization", "Bearer "+token1.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var resultToken2 = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("luis@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token2 = objectMapper.readValue(resultToken2.getResponse().getContentAsString(),TokenDTO.class);
        var result2 = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-33")
                                                .accountTo("025-253568-54")
                                                .amount(100000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token2.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println("Disable account result: "+result1.getResponse().getContentAsString());
        int status = result2.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Transfer operation result: "+message);
    }

    @Test
    public void testTransferMoneyWhenAccountFromIsDepositOnly() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("luis@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-01")
                                                .accountTo("025-253568-33")
                                                .amount(100000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        int status = result.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Transfer operation result: "+message);
    }

    @Test
    public void testTransferMoneyWhenAccountToIsDepositOnly() throws Exception {
        var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO("keren@email.com", "password"))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
        var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
                                objectMapper.writeValueAsString(
                                        TransactionOperationDTO.builder()
                                                .result("")
                                                .accountFrom("025-253568-25")
                                                .accountTo("025-253568-54")
                                                .amount(100000L)
                                                .build()
                                ))
                        .header("Authorization", "Bearer "+token.getToken())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        int status = result.getResponse().getStatus();
        String message = HttpStatus.valueOf(status).getReasonPhrase();
        System.out.println("Transfer operation result: "+message);
    }
}

