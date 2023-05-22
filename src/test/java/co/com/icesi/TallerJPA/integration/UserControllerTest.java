package co.com.icesi.TallerJPA.integration;


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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class UserControllerTest {
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
    public void testUsersEndpointWhenUserNotAuth() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultAdminCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    //These are the tests to validate the creation of a user, bank and admin according to the User type that is authenticated
    //Besides, these tests were done fulfilling the custom anotations and the input validations (@Valid) of UserCreateDTO
    @Test
    public void testUsersEndpointWhenUserAuthAdminToCreateUser() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthAdminToCreateBank() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultBankCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthAdminToCreateAdmin() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultAdminCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthUserToCreateUser() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultUserCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testUsersEndpointWhenUserAuthUserToCreateBank() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultBankCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthUserToCreateAdmin() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultAdminCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenUser))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthBankToCreateUser() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userByBankCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthBankToCreateAdmin() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(defaultAdminCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testUsersEndpointWhenUserAuthBankToCreateBank() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(bankByBankCreateDTO())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenBank))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    //These tests were done to fail the input validations (@Valid)

    @Test   //This test is to fail the input validation of the @NotBlank firstName
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutFirtsName() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @NotBlank lastName
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutLastName() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

        @Test   //This test is to fail the input validation of the @Email email
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithEmailBadFormat() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }


    @Test   //This test is to fail the input validation of the @ColombianNumberConstraint phoneNumber
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithPhoneBadFormat() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @NotBlank password
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutPassword() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test   //This test is to fail the input validation of the @NotBlank role
    public void testUsersEndpointWhenUserAuthAdminToCreateUserWithoutRole() throws Exception{
        var result = mocMvc.perform(MockMvcRequestBuilders.post("/users").content(
                                objectMapper.writeValueAsString(userWithoutAnything())
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenAdmin))
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
    private UserCreateDTO defaultUserCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Carlos")
                .lastName("Gomez")
                .email("test1@hotmail.com")
                .phoneNumber("+573151220529")
                .password("1234")
                .role("USER")
                .build();
    }
    private UserCreateDTO userByBankCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Ana")
                .lastName("Gomez")
                .email("t1@hotmail.com")
                .phoneNumber("+573151220387")
                .password("1234")
                .role("USER")
                .build();
    }


    private UserCreateDTO userWithoutAnything(){
        return UserCreateDTO.builder()
                .firstName("")
                .lastName("")
                .email("aghotmail.com")
                .phoneNumber("3151245687")
                .password("")
                .role("")
                .build();
    }


    private UserCreateDTO defaultBankCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("John")
                .lastName("Gomez")
                .email("test2@hotmail.com")
                .phoneNumber("+573154620559")
                .password("1234")
                .role("BANK")
                .build();
    }
    private UserCreateDTO bankByBankCreateDTO(){
        return UserCreateDTO.builder()
                .firstName("Fanny")
                .lastName("Gomez")
                .email("t2@hotmail.com")
                .phoneNumber("+573150510559")
                .password("1234")
                .role("BANK")
                .build();
    }
}
