package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Object> addNewClient (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();

		String name = (String) payload.get("name");
		Long dailyMessageQuota = Long.valueOf((Integer) payload.get("dailyMessageQuota"));

		Client client = new Client(name, dailyMessageQuota);

		clientRepository.save(client);

		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@DeleteMapping(path="/")
	public ResponseEntity<Object> removeClientWithId (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();
		Long id = (Long) payload.get("id");

		Optional<Client> client = clientRepository.findById(id);

		if (client.isEmpty()) {

			response.put("status", "failed");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}

		clientRepository.deleteById(id);
		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/")
	public ResponseEntity<Object> getClient (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();
		Long id = Long.valueOf((Integer) payload.get("id"));
		Optional<Client> client = clientRepository.findById(id);

		if (client.isEmpty()) {

			response.put("status", "failed");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}


		JsonNode clientNode = mapper.valueToTree(client);
		response.put("status", "success");
		response.set("data", clientNode);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
