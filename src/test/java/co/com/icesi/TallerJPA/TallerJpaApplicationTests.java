package co.com.icesi.TallerJPA;

import co.com.icesi.TallerJPA.dto.requestDTO.IcesiRoleCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.IcesiUserCreateDTO;
import co.com.icesi.TallerJPA.dto.requestDTO.LoginDTO;
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
@SpringBootTest
@Import(TestConfigurationData.class)
@ActiveProfiles(profiles="test")
class TallerJpaApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;


	@Test
	void contextLoads() {
	}

	@Test
	public void testTokenEndPoint() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.get("/token").content(
								objectMapper.writeValueAsString(new LoginDTO("johndoe@email.com", "password")))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	//CREAR PRUEBA PARA CREAR UN USUARIO
	private IcesiRoleCreateDTO getDefaultRoleDTO(){
		return   IcesiRoleCreateDTO.builder()
				.name("Administrador")
				.description("El administrador se encarga de administrar la página")
				.build();
	}
	@Test
	public void testCreateUserEndPoint() throws Exception{
		var result = mockMvc.perform(MockMvcRequestBuilders.get("/users").content(
								objectMapper.writeValueAsString(
										IcesiUserCreateDTO.builder()
												.firstName("John")
												.lastName("Doe")
												.email("johndoe@email.com")
												.phoneNumber("+57123123123")
												.password("password")
												.role(getDefaultRoleDTO())
												.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}
}
