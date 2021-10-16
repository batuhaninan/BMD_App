package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Request;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

	SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);

	@GetMapping(path="/count/request")
	public @ResponseBody ObjectNode getRequestCount (@RequestBody Map<String, Object> payload) throws ParseException {
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;
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
	public @ResponseBody ObjectNode getRequestStats (@RequestBody Map<String, Object> payload) throws ParseException {
		// TODO: Parse JSON
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;
		int success = 0;
		int failed = 0;
		ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAll();
		for (Request request : requests){
			if (request.getStartTime().after(startTime)){
				if (endTime.after(request.getEndTime())){
					if (request.getResultCode() == 0 || request.getResultCode() == 200 ){
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
	public @ResponseBody ObjectNode getRequestErrorCounts (@RequestBody Map<String, Object> payload) throws ParseException {
		Hashtable<Long, Integer> errors = new Hashtable<Long, Integer>();
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;
		ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAll();
		for (Request request : requests){
			if (request.getStartTime().after(startTime)){
				if (endTime.after(request.getEndTime())){
					if(request.getResultCode()!=0){ // fix request.getSuccess
						if (Long.valueOf(request.getResultCode()).equals(null)){
							continue;
						}
						if (errors.get(request.getResultCode()) != null) {
							errors.put(request.getResultCode(), errors.get(request.getResultCode()) + 1);
						}
						else{
							errors.put(request.getResultCode(), 1);
						}
					}
				}
			}
		}
		JsonNode node = mapper.valueToTree(errors);
		response.put("status","success");
		response.set("data",node);
		return response;
	}

	@GetMapping(path="/count/failed/client")
	public @ResponseBody ObjectNode getRequestErrorCountsPerClient (@RequestBody Map<String, Object> payload) throws ParseException {
		// TODO: Parse JSON
		Hashtable<Long,Hashtable<Long, Integer>> errors = new Hashtable<Long, Hashtable<Long, Integer>>();
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;
		ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAll();
		for (Request request : requests){
			if (request.getStartTime().after(startTime)){
				if (endTime.after(request.getEndTime())){
					if(request.getResultCode()!=0){
						Hashtable<Long, Integer> innerMap = new Hashtable<Long, Integer>();
						if(errors.get(request.getClient().getId())!=null){
							if ( errors.get( request.getClient().getId() ).get( request.getResultCode() ) != null ){
								innerMap = errors.get(request.getClient().getId());
								innerMap.put(request.getResultCode(),errors.get(request.getClient().getId()).get(request.getResultCode())+1);
								errors.put(request.getClient().getId(), innerMap);
							}
							else{
								innerMap = errors.get(request.getClient().getId());
								innerMap.put(request.getResultCode(), 1);
								errors.put(request.getClient().getId(), innerMap);
							}
						}
						else{
							innerMap.put(request.getResultCode(), 1);
							errors.put(request.getClient().getId(), innerMap);
						}

					}
				}
			}
		}
		JsonNode node = mapper.valueToTree(errors);
		response.put("status","success");
		response.set("data",node);
		return response;
	}



}
