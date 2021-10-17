package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.bmd_app.bmd_app.Service.ClientService;
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
	private ClientService clientService;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	ObjectMapper mapper;

	@DeleteMapping(path="/")
	public ResponseEntity<Object> removeClientWithId (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();
		Long id = Long.valueOf((Integer) payload.get("id"));
		System.out.println(id);
		Boolean check = clientService.removeClient(id);
		System.out.println(check);

		if (!check) {
			response.put("status", "failed");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/")
	public ResponseEntity<Object> getClient (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();
		Long id = Long.valueOf((Integer) payload.get("id"));
		Optional<Client> client = clientService.getClientWithId(id);


		if (client == null) {

			response.put("status", "failed");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}


		JsonNode clientNode = mapper.valueToTree(client);
		response.put("status", "success");
		response.set("data", clientNode);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
