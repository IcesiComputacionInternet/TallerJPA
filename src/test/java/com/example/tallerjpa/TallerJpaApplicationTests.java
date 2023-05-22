package com.example.tallerjpa;

import com.example.tallerjpa.dto.*;
import com.example.tallerjpa.enums.AccountType;
import com.example.tallerjpa.model.IcesiAccount;
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

	@Test
	void contextLoads() {
	}


	@Test
	public void testCreateUserEndpoint() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/users").content(
				objectMapper.writeValueAsString(
						UserDTO.builder()
								.firstName("")
								.lastName("Doe")
								.email("juan123@hotmail.com")
								.phoneNumber("+573231231240")
								.password("password")
								.role("ADMIN")
								.build()
						)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());

	}

	@Test
	public void testCreateRoleEndpoint() throws Exception {
		var result = mockMvc.perform(MockMvcRequestBuilders.post("/roles").content(
						objectMapper.writeValueAsString(
								RoleDTO.builder()
										.description("role for normal users")
										.name("user")
										.build()
						)).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());

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
