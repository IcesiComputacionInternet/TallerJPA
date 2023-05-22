package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.IcesiRoleDTO;
import co.com.icesi.TallerJPA.dto.IcesiUserDTO;
import co.com.icesi.TallerJPA.dto.LoginDto;
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
public class IcesiUserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    private String token = "";

    private static final String URL= "/user";

    @Test
    public void testCreateUserNoAuth() throws Exception {
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    public void testCreateUserAdmin() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserAdminNonColombianNumber() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiUserDTO.builder()
                                        .firstName("Jhon")
                                        .lastName("Doe")
                                        .phoneNumber("+973322356789")
                                        .password("1234567")
                                        .email("Richie.doe@gmailTest9.com")
                                        .role(IcesiRoleDTO.builder()
                                                .name("NORMAL")
                                                .build())
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserAdminBlankPassword() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiUserDTO.builder()
                                        .firstName("Jhon")
                                        .lastName("Doe")
                                        .phoneNumber("+573322356789")
                                        .password("")
                                        .email("Richie.doe@gmailTest9.com")
                                        .role(IcesiRoleDTO.builder()
                                                .name("NORMAL")
                                                .build())
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserAdminBlankEmail() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiUserDTO.builder()
                                        .firstName("Jhon")
                                        .lastName("Doe")
                                        .phoneNumber("+573322356789")
                                        .password("")
                                        .email("")
                                        .role(IcesiRoleDTO.builder()
                                                .name("NORMAL")
                                                .build())
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserAdminNotFormatEmail() throws Exception {
        setToken("admin@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiUserDTO.builder()
                                        .firstName("Jhon")
                                        .lastName("Doe")
                                        .phoneNumber("+573322356789")
                                        .password("12121212")
                                        .email("Richi.com")
                                        .role(IcesiRoleDTO.builder()
                                                .name("NORMAL")
                                                .build())
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserBank() throws Exception {
        setToken("bank@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(IcesiUserDTO.builder()
                                        .firstName("Jhon")
                                        .lastName("Doe")
                                        .phoneNumber("+573377456789")
                                        .password("12456789")
                                        .email("Bonioe@gmailTest2.com")
                                        .role(IcesiRoleDTO.builder()
                                                .name("NORMAL")
                                                .build())
                                        .build()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void testCreateUserNormal() throws Exception {
        setToken("user@email.com");
        var  result = mockMvc.perform(MockMvcRequestBuilders.post(URL).content(
                                objectMapper.writeValueAsString(defaultUser()))
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
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

    public IcesiUserDTO defaultUser(){
        return  IcesiUserDTO.builder()
                .firstName("Jhon")
                .lastName("Doe")
                .phoneNumber("+573322456789")
                .password("12456789")
                .email("Alex.doe@gmailTest2.com")
                .role(IcesiRoleDTO.builder()
                        .name("NORMAL")
                        .build())
                .build();
    }
}
