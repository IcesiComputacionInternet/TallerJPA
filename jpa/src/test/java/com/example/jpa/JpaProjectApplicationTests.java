package com.example.jpa;

import com.example.jpa.dto.LoginDTO;
import com.example.jpa.error.util.IcesiError;
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

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
public class JpaProjectApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void testTokenEndpointHappyPath() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
				)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
		assertNotEquals("", result.getResponse().getContentAsString());
	}
	@Test
	public void testTokenEndpointUserHappyPath() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
		assertNotEquals("", result.getResponse().getContentAsString());
	}

	@Test
	public void testTokenEndpointBankHappyPath() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
		assertNotEquals("", result.getResponse().getContentAsString());
	}

	@Test
	public void testTokenEndpointWithInvalidEmail() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe4@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andReturn();
		IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
		System.out.println(error);
		assertNotNull(error);
	}

}
