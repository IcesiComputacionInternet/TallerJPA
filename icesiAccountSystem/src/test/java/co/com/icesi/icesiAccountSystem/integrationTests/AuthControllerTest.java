package co.com.icesi.icesiAccountSystem.integrationTests;
import co.com.icesi.icesiAccountSystem.dto.LoginDTO;
import co.com.icesi.icesiAccountSystem.dto.TokenDTO;
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
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles="test")

class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void testTokenEndPoint() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		TokenDTO token =objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		assertNotNull(token);
	}

	@Test
	public void testTokenEndPointWithInvalidEmail() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoeemail.com", "password")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		TokenDTO token =objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		assertNotNull(token);
	}

	@Test
	public void testTokenEndPointWithAnEmailThatDoesNotExists() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("sara@email.com", "password")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		TokenDTO token =objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		assertNotNull(token);
	}

}
