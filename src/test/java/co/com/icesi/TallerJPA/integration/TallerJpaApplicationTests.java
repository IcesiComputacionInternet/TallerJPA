package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.LoginDTO;
import co.com.icesi.TallerJPA.dto.TokenDTO;
import co.com.icesi.TallerJPA.error.exception.ArgumentsError;
import co.com.icesi.TallerJPA.integration.config.TestConfigurationData;
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
public class TallerJpaApplicationTests {

	@Autowired
	private MockMvc mocMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {

	}

	@Test
	public void testTokenEndpointAdmin() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
				)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		//System.out.println("Role: "+IcesiSecurityContext.getCurrentUserRole());
		System.out.println(token);
		assertNotNull(token);
	}

	@Test
	public void testTokenEndpointUser() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		//System.out.println("Role: "+IcesiSecurityContext.getCurrentUserRole());
		System.out.println(token);
		assertNotNull(token);
	}

	@Test
	public void testTokenEndpointBank() throws Exception{
		var result = mocMvc.perform(MockMvcRequestBuilders.post("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe3@email.com","password"))
						)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		TokenDTO token = objectMapper.readValue(result.getResponse().getContentAsString(), TokenDTO.class);
		//System.out.println("Role: "+IcesiSecurityContext.getCurrentUserRole());
		System.out.println(token);
		assertNotNull(token);
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


