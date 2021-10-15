package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(path="/api/client")
public class ClientController {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	ObjectMapper mapper;

	@PostMapping(path="/")
	public @ResponseBody ObjectNode addNewClient (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();

		String name = (String) payload.get("name");
		Integer dailyMessageQuota = (Integer) payload.get("dailyMessageQuota");

		Client client = new Client(name, dailyMessageQuota.longValue());

		clientRepository.save(client);

		//Optional<Client> savedClient = clientRepository.findById(client.getId().intValue());

		//if (savedClient.isEmpty()) {

			//response.put("status", "failed");
			//return response;
		//}

		response.put("status", "success");
		return response;
	}

	@DeleteMapping(path="/")
	public @ResponseBody
	ObjectNode removeClientWithId (@RequestParam Integer id){
		ObjectNode response = mapper.createObjectNode();

		Optional<Client> client = clientRepository.findById(id);

		if (client.isEmpty()) {

			response.put("status", "failed");
			return response;
		}

		clientRepository.deleteById(id);
		response.put("status", "success");
		return response;
	}

	@GetMapping(path="/")
	public @ResponseBody ObjectNode getClient (@RequestParam Integer id){
		ObjectNode response = mapper.createObjectNode();
		Optional<Client> client = clientRepository.findById(id);

		if (client.isEmpty()) {

			response.put("status", "failed");
			return response;
		}

		response.put("status", "success");
		return response;
	}
}
