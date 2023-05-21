package com.example.TallerJPA;

import com.example.TallerJPA.dto.LoginDTO;
import com.example.TallerJPA.dto.TokenDTO;
import com.example.TallerJPA.dto.TransferRequestDTO;
import com.example.TallerJPA.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class TallerJpaApplicationTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	private static String token = "";

	@Test
	void contextLoads() {
	}
	@Test
	@Order(1)
	public void testTokenEndpoint() throws Exception{
			var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
					objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
			)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		TokenDTO tokenDTO = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
		token = tokenDTO.getToken();
		assertNotNull(result.getResponse().getContentAsString());
	}
	@Test
	@Order(2)
	public void testCreateUserWhenUserIsAllowed() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/users").content(
				objectMapper.writeValueAsString(new UserDTO("Pedro",
						"Perez",
						"pedro@email.com",
						"3205785410",
						"password",
						"USER")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header("Authorization","Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	@Order(3)
	public void testCreateUserWhenUserIsNotAllowed() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/users").content(
								objectMapper.writeValueAsString(new UserDTO("Pedro",
										"Perez",
										"pedro@email.com",
										"3205785410",
										"password",
										"USER")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	@Order(4)
	public void testTransferMoneyWhenUserIsNotAllowed() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/transfer").content(
				objectMapper.writeValueAsString(
						TransferRequestDTO.builder()
								.originAccountNumber("1234567890")
								.destinationAccountNumber("0987654321")
								.amount(1000L)
								.build()
				))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}
	@Test
	@Order(5)
	public void testTransferMoneyWhenUserIsAllowed() throws Exception{
		String tokenSt = getUserToken();
		var result = mockMvc.perform(MockMvcRequestBuilders.patch("/accounts/testPath/").content(
				objectMapper.writeValueAsString(
						TransferRequestDTO.builder()
								.originAccountNumber("1234567890")
								.destinationAccountNumber("0987654321")
								.amount(1000L)
								.build()
				))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization","Bearer " + tokenSt))
				.andExpect(status().isOk())
				.andReturn();
	}

	private String getUserToken() throws Exception {
		var token = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		TokenDTO tokenDTO = objectMapper.readValue(token.getResponse().getContentAsString(),TokenDTO.class);
		return tokenDTO.getToken();
	}



}
