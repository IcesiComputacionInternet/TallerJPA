package com.example.demo;

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

import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.TokenDTO;
import com.example.demo.error.exception.IcesiError;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class DemoApplicationTests {


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
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
				)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	/*@Test
	public void testTokenEndpointWithINvalidEmail() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.get("/token").content(
			objectMapper.writeValueAsString(new LoginDTO("incorrect@email.com", "password"))
		)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.andExpect(status().isOk())
		.andReturn();
		IcesiError token = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
		assertNotNull(token); 
	}*/
}
