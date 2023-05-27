package com.edu.icesi.demojpa.integration;

import com.edu.icesi.demojpa.dto.request.LoginDTO;
import com.edu.icesi.demojpa.dto.request.RequestUserDTO;
import com.edu.icesi.demojpa.dto.request.TokenDTO;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = "test")
@Import(TestConfigurationData.class)
class DemoJpaApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void testLoginEndPoint() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
		assertNotNull(token);
	}

	@Test
	public void testLoginInvalidEmail() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("jhondoe@email.com", "password")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void testLoginInvalidPassword() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "paswor")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	public void testCreateAccountByAdmin() throws Exception {
		var resultToken = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		TokenDTO token = objectMapper.readValue(resultToken.getResponse().getContentAsString(),TokenDTO.class);
		mockMvc.perform(MockMvcRequestBuilders.post("/users/create").content(
						objectMapper.writeValueAsString(
								RequestUserDTO.builder()
										.email("luismiguelossaarias05@gmail.com")
										.phoneNumber("+573174833968")
										.firstName("Luis Miguel")
										.lastName("Ossa Arias")
										.password("password")
										.role("USER")
										.build()
						))
						.header("Authorization", "Bearer " + token.getToken())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}
}
