package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

	@PostMapping(path="/")
	public @ResponseBody Iterable<Client> addNewClient (){
		// TODO: Parse JSON

		return clientRepository.findAll();
	}

	@DeleteMapping(path="/")
	public @ResponseBody
	Map<String, String> removeClientWithId (@RequestParam Integer id){
		HashMap<String, String> response = new HashMap<>();
		Optional<Client> client = clientRepository.findById(id);

		if (client.isEmpty()) {
			// TODO: Return approp. message.
			response.put("status", "failed");

			return response;
		}

		response.put("status", "success");
		return response;
	}

	@GetMapping(path="/")
	public @ResponseBody Optional<Client> getClient (@RequestParam Integer id){

		Optional<Client> client = clientRepository.findById(id);

		if (client.isEmpty()) {
			// TODO: Return approp. message

			return null;
		}

		return client;
	}




}
