package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/api/query")
public class QueryController {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	RequestRepository requestRepository;

	@GetMapping(path="/count/request")
	public @ResponseBody Iterable<Client> getRequestCount (){
		// TODO: Parse JSON

		return clientRepository.findAll();
	}

	@GetMapping(path="/count/stats")
	public @ResponseBody Iterable<Client> getRequestStats (){
		// TODO: Parse JSON

		return clientRepository.findAll();
	}

	@GetMapping(path="/count/failed/")
	public @ResponseBody Iterable<Client> getRequestErrorCounts (@RequestParam Integer errorCode){
		// TODO: Parse JSON

		return clientRepository.findAll();
	}

	@GetMapping(path="/count/failed/")
	public @ResponseBody Iterable<Client> getRequestErrorCountsPerClient (@RequestParam Integer  errorCode, @RequestParam String clientId){
		// TODO: Parse JSON

		return clientRepository.findAll();
	}



}
