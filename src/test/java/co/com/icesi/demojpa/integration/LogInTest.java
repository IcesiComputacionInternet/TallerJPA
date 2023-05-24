package co.com.icesi.demojpa.integration;



import co.com.icesi.demojpa.TestConfigurationData;
import co.com.icesi.demojpa.dto.LoginDTO;
import co.com.icesi.demojpa.dto.TokenDTO;
import co.com.icesi.demojpa.error.exception.IcesiError;
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
class LogInTest {

	@Autowired
	private MockMvc mocMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void testTokenEndpointHappyPath() throws Exception {
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		TokenDTO tokenDTO = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		assertNotNull(tokenDTO);
	}

	@Test
	public void testTokenEndpointInvalidUser() throws Exception {
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email4.com", "password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andReturn();

		IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
		assertNotNull(error);
	}

	@Test
	public void testTokenEndpointPassword() throws Exception {
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "NotApassword"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is5xxServerError())
				.andReturn();

		IcesiError error = objectMapper.readValue(result.getResponse().getContentAsString(), IcesiError.class);
		assertNotNull(error);
		assertEquals("Bad credentials", error.getDetails());
	}
}

