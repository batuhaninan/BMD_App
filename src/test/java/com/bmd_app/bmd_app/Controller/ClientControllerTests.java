package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	ClientRepository clientRepository;

	@Test
	public void shouldReturnUnauthorized() throws Exception {
		this.mockMvc.perform(get("/api/client/")).andDo(print()).andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldReturnOk() throws Exception {

		clientRepository.deleteAll();

		Client client = new Client();
		client.setEmail("inanbatuhan61@gmail.com");
		client.setDailyMessageQuota(15L);
		client.setPassword("ELMARMUT");
		client.setName("Batuhan INAN");

		clientRepository.save(client);

		HashMap<String, Object> postRequestJSON = new HashMap<>();
		postRequestJSON.put("email", "inanbatuhan61@gmail.com");
		postRequestJSON.put("password", "ELMARMUT");

		HashMap<String, Object> getRequestJSON = new HashMap<>();
		getRequestJSON.put("id", "5");

		this.mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(postRequestJSON))).andExpect(status().isOk());
		this.mockMvc.perform(get("/api/client/").content(mapper.writeValueAsString(postRequestJSON))).andDo(print()).andExpect(status().isOk());
	}

}
