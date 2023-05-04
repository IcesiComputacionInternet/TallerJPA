package co.edu.icesi.demo;

import co.edu.icesi.demo.dto.LoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jcip.annotations.Immutable;
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
@SpringBootTest
@ActiveProfiles(profiles="test")
@Import(TestConfigurationData.class)
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;
	@Test
	void contextLoads() {
	}

	@Test public void testTokenEndpoint() throws Exception{
		var result= mockMvc.perform(MockMvcRequestBuilders.get("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com","password"))
				)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

				System.out.println(result.getResponse().getContentAsString());
	}

}
