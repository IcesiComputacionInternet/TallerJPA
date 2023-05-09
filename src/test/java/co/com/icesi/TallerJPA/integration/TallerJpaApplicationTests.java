package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.LoginDTO;
import co.com.icesi.TallerJPA.dto.TokenDTO;
import co.com.icesi.TallerJPA.dto.UserCreateDTO;
import co.com.icesi.TallerJPA.error.exception.ArgumentsError;
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
	public void testTokenEndpoint() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
				)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		assertNotNull(token);
		//System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testTokenEndpointWithInvalidEmail() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		ArgumentsError token = objectMapper.readValue(result.getResponse().getContentAsString(), ArgumentsError.class);
		assertNotNull(token);
		//System.out.println(result.getResponse().getContentAsString());
	}


}


