package co.com.icesi.TallerJPA.integration;

import co.com.icesi.TallerJPA.dto.LoginDto;
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

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfigurationData.class )
@ActiveProfiles(profiles = "test")
class TallerJpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	public void testTokenEndpoint() throws Exception{
		var  result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").content(
								objectMapper.writeValueAsString(new LoginDto("admin@email.com","password")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		System.out.println(result.getResponse().getContentAsString());

	}





}
