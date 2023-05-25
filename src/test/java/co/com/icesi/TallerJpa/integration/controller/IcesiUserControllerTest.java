package co.com.icesi.TallerJpa.integration.controller;

import co.com.icesi.TallerJpa.controller.api.IcesiUserApi;
import co.com.icesi.TallerJpa.dto.*;
import co.com.icesi.TallerJpa.error.exception.ErrorCode;
import co.com.icesi.TallerJpa.error.exception.IcesiError;
import co.com.icesi.TallerJpa.error.exception.IcesiErrorDetail;
import co.com.icesi.TallerJpa.integration.configuration.TestConfigurationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static co.com.icesi.TallerJpa.util.TestItemsBuilder.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
public class IcesiUserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    static TokenDTO userTokenDTO;

    static TokenDTO bankTokenDTO;

    static TokenDTO adminTokenDTO;

    @BeforeEach
    public void init() {
        userTokenDTO = loginAsUser();
        bankTokenDTO = loginAsBank();
        adminTokenDTO = loginAsAdmin();
    }

    @Test
    @DisplayName("Create an icesi user when user is not authenticated.")
    public void testCreateIcesiUserWhenNotAuthenticated() throws Exception {
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .content(mapper.writeValueAsString(defaultUserIcesiUserDTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Create an icesi user whit user credentials.")
    public void testCreateIcesiUserWithUserCredentials() throws Exception {
        var user = defaultUserIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+userTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Create an icesi user with role USER with admin credentials")
    public void testCreateIcesiUserWithAdminCredentials() throws Exception {
        var user = defaultUserIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserResponseDTO icesiUserResponseDTO = mapper.readValue(result.getResponse().getContentAsString(), IcesiUserResponseDTO.class);
        assertNotNull(icesiUserResponseDTO);
        assertNotNull(login(user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("Create an icesi user with role USER with bank credentials")
    public void testCreateIcesiUserWithBankCredentials() throws Exception {
        var user = defaultUserIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+bankTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserResponseDTO icesiUserResponseDTO = mapper.readValue(result.getResponse().getContentAsString(), IcesiUserResponseDTO.class);
        assertNotNull(icesiUserResponseDTO);
        assertNotNull(login(user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("Create an icesi user with role BANK with admin credentials")
    public void testCreateIcesiBankWithAdminCredentials() throws Exception {
        var user = defaultBankIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserResponseDTO icesiUserResponseDTO = mapper.readValue(result.getResponse().getContentAsString(), IcesiUserResponseDTO.class);
        assertNotNull(icesiUserResponseDTO);
        assertNotNull(login(user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("Create an icesi user with role BANK with bank credentials")
    public void testCreateIcesiBankWithBankCredentials() throws Exception {
        var user = defaultBankIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+bankTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserResponseDTO icesiUserResponseDTO = mapper.readValue(result.getResponse().getContentAsString(), IcesiUserResponseDTO.class);
        assertNotNull(icesiUserResponseDTO);
        assertNotNull(login(user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("Create an icesi user with role ADMIN with admin credentials")
    public void testCreateIcesiAdminWithAdminCredentials() throws Exception {
        var user = defaultAdminIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        IcesiUserResponseDTO icesiUserResponseDTO = mapper.readValue(result.getResponse().getContentAsString(), IcesiUserResponseDTO.class);
        assertNotNull(icesiUserResponseDTO);
        assertNotNull(login(user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("Create an icesi user with role ADMIN with bank credentials")
    public void testCreateIcesiAdminWithBankCredentials() throws Exception {
        var user = defaultAdminIcesiUserDTO();
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+bankTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        List<IcesiErrorDetail> icesiErrorDetailList = icesiError.getDetails();
        assertEquals(HttpStatus.FORBIDDEN, icesiError.getStatus());
        assertEquals(icesiErrorDetailList.size(), 1);
        assertEquals(icesiErrorDetailList.get(0).getErrorCode(), ErrorCode.ERR_403.getCode());
        assertEquals("Forbidden. Only ADMINs can create ADMIN users", icesiErrorDetailList.get(0).getErrorMessage());
    }

    @Test
    @DisplayName("Create an icesi user with role ADMIN and null firstName")
    public void testCreateIcesiUserWithAdminCredentialsAndNullFirstName() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setFirstName(null);
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field firstName can't be null.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and blank firstName")
    public void testCreateIcesiUserWithAdminCredentialsAndBlankFirstName() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setFirstName(" ");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field firstName can't be blank.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and empty firstName")
    public void testCreateIcesiUserWithAdminCredentialsAndEmptyFirstName() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setFirstName("");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field firstName can't be empty.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and null lastName")
    public void testCreateIcesiUserWithAdminCredentialsAndNullLastName() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setLastName(null);
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field lastName can't be null.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and blank lastName")
    public void testCreateIcesiUserWithAdminCredentialsAndBlankLastName() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setLastName(" ");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field lastName can't be blank.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and empty lastName")
    public void testCreateIcesiUserWithAdminCredentialsAndEmptyLastName() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setLastName("");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field lastName can't be empty.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and null email")
    public void testCreateIcesiUserWithAdminCredentialsAndNullEmail() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setEmail(null);
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field email can't be null.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and blank email")
    public void testCreateIcesiUserWithAdminCredentialsAndBlankEmail() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setEmail(" ");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field email can't be blank.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and empty email")
    public void testCreateIcesiUserWithAdminCredentialsAndEmptyEmail() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setEmail("");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field email can't be empty.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and invalid email")
    public void testCreateIcesiUserWithAdminCredentialsAndInvalidEmail() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setEmail("asd");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field email must be a well-formed email address");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and null phoneNumber")
    public void testCreateIcesiUserWithAdminCredentialsAndNullPhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber(null);
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber can't be null.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and blank phoneNumber")
    public void testCreateIcesiUserWithAdminCredentialsAndBlankPhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber(" ");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber can't be blank.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and empty phoneNumber")
    public void testCreateIcesiUserWithAdminCredentialsAndEmptyPhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber("");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber can't be empty.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and invalid phoneNumber, wrong code")
    public void testCreateIcesiUserWithAdminCredentialsAndInvalid1PhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber("+59 3147424776");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber Numero Invalido, no es de Colombia.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and invalid phoneNumber, wrong symbol")
    public void testCreateIcesiUserWithAdminCredentialsAndInvalid2PhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber("*59 3147424776");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber Numero Invalido, no es de Colombia.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and invalid phoneNumber, wrong length")
    public void testCreateIcesiUserWithAdminCredentialsAndInvalid3PhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber("+57 31474247761");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber Numero Invalido, no es de Colombia.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and invalid phoneNumber, wrong start")
    public void testCreateIcesiUserWithAdminCredentialsAndInvalid4PhoneNumber() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPhoneNumber("+59 2157424776");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field phoneNumber Numero Invalido, no es de Colombia.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and null password")
    public void testCreateIcesiUserWithAdminCredentialsAndNullPassword() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPassword(null);
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field password can't be null.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and blank password")
    public void testCreateIcesiUserWithAdminCredentialsAndBlankPassword() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPassword(" ");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field password can't be blank.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and empty password")
    public void testCreateIcesiUserWithAdminCredentialsAndEmptyPassword() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setPassword("");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field password can't be empty.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and null role")
    public void testCreateIcesiUserWithAdminCredentialsAndNullRole() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setRole(null);
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field role can't be null.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and blank role")
    public void testCreateIcesiUserWithAdminCredentialsAndBlankRole() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setRole(" ");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field role can't be blank.");
    }
    @Test
    @DisplayName("Create an icesi user with role ADMIN and empty role")
    public void testCreateIcesiUserWithAdminCredentialsAndEmptyRole() throws Exception{
        var user = defaultAdminIcesiUserDTO();
        user.setRole("");
        var result = mvc.perform(MockMvcRequestBuilders.post(IcesiUserApi.USER_BASE_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+adminTokenDTO.getToken())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        IcesiError icesiError = mapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        checkIfErrorIsInTheResponse(icesiError, "field role can't be empty.");
    }
    private TokenDTO loginAsAdmin() {
        return login("johndoe.admin@hotmail.com", "password");
    }
    private TokenDTO loginAsUser() {
        return login("johndoe.user@hotmail.com", "password");
    }
    private TokenDTO loginAsBank() {
        return login("johndoe.bank@hotmail.com", "password");
    }

    private TokenDTO login(String username, String password) {
        try{
            var result = mvc.perform(MockMvcRequestBuilders.post("/token").content(
                                    mapper.writeValueAsString(new LoginDTO(username,password))
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
            System.out.println(result.getResponse().getContentAsString());
            TokenDTO token = mapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
            assertNotNull(token);
            return token;
        }catch (Exception e ){
            return null;
        }
    }

    private void checkIfErrorIsInTheResponse(IcesiError icesiError, String expectedError){
        assertEquals(HttpStatus.BAD_REQUEST, icesiError.getStatus());
        boolean e = icesiError.getDetails().stream()
                .anyMatch(error -> error.getErrorMessage().equals(expectedError));
        assertTrue(e);
    }
}
