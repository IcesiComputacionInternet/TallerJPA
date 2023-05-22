package com.example.jpa;

import com.example.jpa.dto.LoginDTO;
import com.example.jpa.dto.UserDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class JpaProjectApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void testTokenEndpoint() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.get("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
				)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}
	@Test
	public void testTokenEndpointUser() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	public void testTokenEndpointBank() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

	}

	@Test
	public void testTokenEndpointWithInvalidEmail() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}

}
