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
import java.util.Map;

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
	public @ResponseBody ObjectNode getRequestCount (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();
		Date startTime = (Date) payload.get("startTime");
		Date endTime = (Date) payload.get("endTime");
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
	public @ResponseBody ObjectNode getRequestStats (@RequestBody Map<String, Object> payload){
		// TODO: Parse JSON
		ObjectNode response = mapper.createObjectNode();
		Date startTime = (Date) payload.get("startTime");
		Date endTime = (Date) payload.get("endTime");
		int success = 0;
		int failed = 0;
		ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAll();
		for (Request request : requests){
			if (request.getStartTime().after(startTime)){
				if (endTime.after(request.getEndTime())){
					if (request.getSuccess()){
						success++;
					}
					else{
						failed++;
					}
				}
			}
		}
		response.put("status","success");
		response.put("success", success);
		response.put("failed",failed);
		return response;
	}

	@GetMapping(path="/count/failed/")
	public @ResponseBody ObjectNode getRequestErrorCounts (@RequestParam Integer errorCode){
		ObjectNode response = mapper.createObjectNode();


		return response;
	}

	@GetMapping(path="/count/failed/client")
	public @ResponseBody ObjectNode getRequestErrorCountsPerClient (@RequestParam Integer  errorCode, @RequestParam String clientId){
		// TODO: Parse JSON
		ObjectNode response = mapper.createObjectNode();


		return response;
	}



}
