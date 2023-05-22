package co.com.icesi.demojpa;



import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TransactionDTO;
import co.com.icesi.demojpa.dto.UserCreateDTO;
import co.com.icesi.demojpa.dto.response.ResponseUserDTO;
import co.com.icesi.demojpa.error.exception.IcesiError;
import co.com.icesi.demojpa.error.exception.IcesiException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class TallerJpaApplicationTests {

	@Autowired
	private MockMvc mocMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {

	}

	@Test
	public void testTokenEndpointHappyPath() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		assertNotNull(result);
	}

	@Test
	public void testTokenEndpointInvalidUser() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email4.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andReturn();

		IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
		System.out.println(error.getDetails());
		assertNotNull(error);
	}

	@Test
	public void testTokenEndpointPassword() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","NotApassword"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andReturn();

		IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
		System.out.println(error.getDetails());
		assertNotNull(error);
		assertEquals("Bad credentials", error.getDetails());
	}

	@Test
	public void testCreateAdminUserEndpointHappyPath() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
								objectMapper.writeValueAsString(new UserCreateDTO(
										"nombre",
										"apellido",
										"123@gmail.co",
										"+57 305 123 432",
										"123",
										"ADMIN")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		ResponseUserDTO user = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), ResponseUserDTO.class);
		assertNotNull(user);
	}

	@Test
	public void testCreateAdminUserNotValidEndpoint() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
								objectMapper.writeValueAsString(new UserCreateDTO(
										"nombre",
										"apellido",
										"123@gmail.co",
										"+57 305 123 432",
										"123",
										"ADMIN")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
		IcesiException exception = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiException.class);
		assertNotNull(exception);
	}

	@Test
	public void testCreateUserEndpointHappyPath() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
								objectMapper.writeValueAsString(new UserCreateDTO(
										"nombre",
										"apellido",
										"123@gmail.co",
										"+57 305 123 432",
										"123",
										"USER")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		assertTrue(result.andReturn().getResponse().getContentAsString().contains("USER"));
	}

	@Test
	public void testCreateUserEndpointInvalidRole() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/users/").content(
								objectMapper.writeValueAsString(new UserCreateDTO(
										"nombre",
										"apellido",
										"123@gmail.co",
										"+57 305 123 432",
										"123",
										"a")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
		System.out.println(result.andReturn().getResponse().getContentAsString());
		IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
		assertNotNull(error);

		assertEquals("ROLE_NOT_FOUND", error.getDetails());
	}

	@Test
	public void testTransferEndpointHappyPath() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
								objectMapper.writeValueAsString(TransactionDTO.builder()
										.accountNumberOrigin("1234567899")
										.accountNumberDestination("123456789")
										.amount(1000L)
										.resultMessage("")
										.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		System.out.println(result.andReturn().getResponse().getContentAsString());
		TransactionDTO transactionDTO = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), TransactionDTO.class);
		assertEquals("El nuevo balance es de: 999000", transactionDTO.getResultMessage());
	}

	@Test
	public void testTransferEndpointBankRole() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
								objectMapper.writeValueAsString(TransactionDTO.builder()
										.accountNumberOrigin("1234567899")
										.accountNumberDestination("123456789")
										.amount(1000L)
										.resultMessage("")
										.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void testTransferEndpointInValidAccNumber() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
								objectMapper.writeValueAsString(TransactionDTO.builder()
										.accountNumberOrigin("1234567899")
										.accountNumberDestination("123456788")
										.amount(1000L)
										.resultMessage("")
										.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
		System.out.println(result.andReturn().getResponse().getContentAsString());
		IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
		assertEquals("ACCOUNT_NOT_FOUND", error.getDetails());
	}

	@Test
	public void testTransferEndpointBalance() throws Exception {
		mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/accounts/transfer/").content(
								objectMapper.writeValueAsString(TransactionDTO.builder()
										.accountNumberOrigin("1234567899")
										.accountNumberDestination("123456789")
										.amount(10000000000000L)
										.resultMessage("")
										.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError());
		System.out.println(result.andReturn().getResponse().getContentAsString());
		IcesiError error = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), IcesiError.class);
		assertEquals("La cuenta que manda el dinero no tiene balance suficiente ", error.getDetails());
	}


}

