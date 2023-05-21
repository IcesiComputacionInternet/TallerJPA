package co.edu.icesi.tallerjpa.controller.integration;

import co.edu.icesi.tallerjpa.TestConfigurationData;
import co.edu.icesi.tallerjpa.api.IcesiUserApi;
import co.edu.icesi.tallerjpa.dto.*;
import co.edu.icesi.tallerjpa.error.exception.ErrorCode;
import co.edu.icesi.tallerjpa.error.exception.IcesiError;
import co.edu.icesi.tallerjpa.error.exception.IcesiErrorDetail;
import co.edu.icesi.tallerjpa.repository.IcesiUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static co.edu.icesi.tallerjpa.util.DTOBuilder.adminIcesiUserCreateDTO;
import static co.edu.icesi.tallerjpa.util.DTOBuilder.defaultIcesiUserCreateDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class IcesiUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    TokenDTO tokenDTO;

    @Autowired
    IcesiUserRepository icesiUserRepository;

    private TokenDTO login(String username, String password) throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
                                objectMapper.writeValueAsString(new LoginDTO(username,password))
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
        assertNotNull(token);
        return token;
    }

    private void loginAsAdmin() throws Exception {
        tokenDTO = login("johndoe@email.com", "password");
    }

    private void loginAsUser() throws Exception {
        tokenDTO = login("johndoe2@email.com", "password");
    }

    private void loginAsBank() throws Exception {
        tokenDTO = login("johndoe3@email.com", "password");
    }

    private void checkThatIcesiUsersDTOsAreTheSame(IcesiUserCreateDTO icesiUserCreateDTO, IcesiUserShowDTO icesiUserShowDTO){
        assertNotNull(icesiUserShowDTO.getUserId());
        assertTrue(Objects.equals(icesiUserCreateDTO.getFirstName(), icesiUserShowDTO.getFirstName()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getLastName(), icesiUserShowDTO.getLastName()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getEmail(), icesiUserShowDTO.getEmail()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getPhoneNumber(), icesiUserShowDTO.getPhoneNumber()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getIcesiRoleCreateDTO().getDescription(), icesiUserShowDTO.getIcesiRole().getDescription()));
        assertTrue(Objects.equals(icesiUserCreateDTO.getIcesiRoleCreateDTO().getName(), icesiUserShowDTO.getIcesiRole().getName()));
        icesiUserRepository.deleteById(icesiUserShowDTO.getUserId());
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentials() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
          objectMapper.writeValueAsString(icesiUserCreateDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
        checkThatIcesiUsersDTOsAreTheSame(icesiUserCreateDTO, icesiUserShowDTO);
    }

    @Test
    public void testEndpointForCreateAUserWithBankCredentials() throws Exception {
        loginAsBank();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
        checkThatIcesiUsersDTOsAreTheSame(icesiUserCreateDTO, icesiUserShowDTO);
    }

    @Test
    public void testEndpointForCreateAnAdminWithAdminCredentials() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = adminIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserShowDTO icesiUserShowDTO = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiUserShowDTO.class);
        checkThatIcesiUsersDTOsAreTheSame(icesiUserCreateDTO, icesiUserShowDTO);
    }

    @Test
    public void testEndpointForCreateAnAdminWithBankCredentials() throws Exception {
        loginAsBank();
        IcesiUserCreateDTO icesiUserCreateDTO = adminIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.FORBIDDEN, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(icesiErrorDetailList.get(0).getErrorCode(), ErrorCode.ERR_403.getCode());
        assertEquals(icesiErrorDetailList.get(0).getErrorMessage(), "Forbidden. Only ADMIN users can create ADMIN users");
    }

    @Test
    public void testEndpointForCreateAnAdminWithUserCredentials() throws Exception {
        loginAsUser();
        IcesiUserCreateDTO icesiUserCreateDTO = adminIcesiUserCreateDTO();
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().isEmpty());
    }

    private void checkIfErrorDetailIsInTheList(IcesiError icesiError, String errorMessage){
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        boolean foundTheErrorMessage = false;
        for(IcesiErrorDetail icesiErrorDetail: icesiErrorDetailList){
            assertEquals(icesiErrorDetail.getErrorCode(), ErrorCode.ERR_400.getCode());
            if(icesiErrorDetail.getErrorMessage().equals(errorMessage)){
                foundTheErrorMessage = true;
                break;
            }
        }
        assertTrue(foundTheErrorMessage);
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullFirstName() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setFirstName(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field firstName: The first name can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankFirstName() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setFirstName(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field firstName: The first name can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyFirstName() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setFirstName("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field firstName: The first name can not be empty");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullLastName() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setLastName(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field lastName: The last name can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankLastName() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setLastName(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field lastName: The last name can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyLastName() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setLastName("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field lastName: The last name can not be empty");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullEmail() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setEmail(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field email: The email can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankEmail() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setEmail(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field email: The email can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyEmail() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setEmail("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field email: The email can not be empty");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndInvalidEmail() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setEmail("pepitoperezgmail.com");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field email: The email must be a well-formed email address");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullPhoneNumber() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setPhoneNumber(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field phoneNumber: The phone number can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankPhoneNumber() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setPhoneNumber(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field phoneNumber: The phone number can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyPhoneNumber() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setPhoneNumber("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field phoneNumber: The phone number can not be empty");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullPassword() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setPassword(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field password: The password can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankPassword() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setPassword(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field password: The password can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyPassword() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setPassword("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field password: The password can not be empty");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.setIcesiRoleCreateDTO(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO: The role of the icesi user can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullDescriptionInTheIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.getIcesiRoleCreateDTO().setDescription(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO.description: The description can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankDescriptionInTheIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.getIcesiRoleCreateDTO().setDescription(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO.description: The description can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyDescriptionInTheIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.getIcesiRoleCreateDTO().setDescription("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO.description: The description can not be empty");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndNullNameInTheIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.getIcesiRoleCreateDTO().setName(null);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO.name: The name of the role can not be null");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndBlankNameInTheIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.getIcesiRoleCreateDTO().setName(" ");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO.name: The name of the role can not be blank");
    }

    @Test
    public void testEndpointForCreateAUserWithAdminCredentialsAndEmptyNameInTheIcesiRoleCreateDTO() throws Exception {
        loginAsAdmin();
        IcesiUserCreateDTO icesiUserCreateDTO = defaultIcesiUserCreateDTO();
        icesiUserCreateDTO.getIcesiRoleCreateDTO().setName("");
        var result = mockMvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.ROOT_PATH)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+tokenDTO.getToken())
                        .content(
                                objectMapper.writeValueAsString(icesiUserCreateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        checkIfErrorDetailIsInTheList(icesiError, "field icesiRoleCreateDTO.name: The name of the role can not be empty");
    }
}
