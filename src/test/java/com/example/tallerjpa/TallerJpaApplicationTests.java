package com.example.tallerjpa;

import com.example.tallerjpa.dto.LoginDTO;
import com.example.tallerjpa.dto.UserDTO;
import com.example.tallerjpa.model.IcesiRole;
import com.example.tallerjpa.model.IcesiUser;
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

import java.util.UUID;

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

	@Test
	void contextLoads() {
	}

	@Test
	public void testCreateUserEndpoint() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/users").content(
				objectMapper.writeValueAsString(
						UserDTO.builder()
								.firstName("John")
								.lastName("Doe")
								.email("juan123@hotmail.com")
								.phoneNumber("+57123123124")
								.password("password")
								.role("ADMIN")
								.build()
						)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());

	}

	@Test
	public void testTokenEndpoint() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/token").content(
				objectMapper.writeValueAsString(new LoginDTO("johndoe2@email.com", "password"))
				)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}


}
