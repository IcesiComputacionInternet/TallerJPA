package com.example.tallerjpa.integration;

import com.example.tallerjpa.dto.LoginDTO;
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
@Import(TestConfigurationData.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
class TallerJpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	private String token = "";

	@Test
	void contextLoads() {
	}

	@Test
	public void testTokenEndpoint() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
				)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}


}
