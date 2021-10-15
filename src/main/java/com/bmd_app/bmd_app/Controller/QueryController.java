package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Entity.Request;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@Controller
@RequestMapping(path="/api/query")
public class QueryController {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	ObjectMapper mapper;

	@GetMapping(path="/count/request")
	public @ResponseBody ObjectNode getRequestCount (@RequestBody Date startTime, @RequestBody Date endTime){
		ObjectNode response = mapper.createObjectNode();
		int count = 0;
		ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAll();
		for (Request request : requests){
			if (request.getStartTime().after(startTime)){
				if (endTime.after(request.getEndTime())){
					count++;
				}
			}
		}
		response.put("status","success");
		response.put("count",count);
		return response;
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
