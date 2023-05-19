package com.example.TallerJPA;

import com.example.TallerJPA.dto.LoginDTO;
import com.example.TallerJPA.dto.TokenDTO;
import com.example.TallerJPA.dto.UserDTO;
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

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles = "test")
class TallerJpaApplicationTests {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}
	@Test
	public void testTokenEndpoint() throws Exception{
			var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
					objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
			)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		TokenDTO tokenDTO = objectMapper.readValue(result.getResponse().getContentAsString(),TokenDTO.class);
		assertNotNull(result.getResponse().getContentAsString());
	}
	@Test
	public void testCreateUser() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/users").content(
				objectMapper.writeValueAsString(new UserDTO("Pedro",
						"Perez",
						"pedro@email.com",
						"3205785410",
						"password",
						"USER")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
	}

}
