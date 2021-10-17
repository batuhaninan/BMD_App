package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.bmd_app.bmd_app.Service.QueryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path="/api/query")
public class QueryController {

	@Autowired
	private QueryService queryService;

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
	public ResponseEntity<Object> getRequestCount (@RequestBody Map<String, Object> payload) throws ParseException {

		ObjectNode response = mapper.createObjectNode();

		Date startTime = formatter.parse((String) payload.get("startTime"));
		Date endTime = formatter.parse((String) payload.get("endTime"));

		Hashtable<Long, Integer> requestsByClient = queryService.requestCount(startTime, endTime);

		JsonNode node = mapper.valueToTree(requestsByClient);

		response.put("status","success");
		response.set("data",node);

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/count/stats")
	public ResponseEntity<Object> getRequestStats (@RequestBody Map<String, Object> payload) throws ParseException {
		// TODO: Parse JSON

		ObjectNode response = mapper.createObjectNode();

		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;

		Hashtable<String, Integer> stats = queryService.requestStats(startTime, endTime);

		response.put("status","success");
		response.put("successCount", stats.get("success"));
		response.put("failCount",stats.get("failed"));

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/count/failed/")
	public ResponseEntity<Object> getRequestErrorCounts (@RequestBody Map<String, Object> payload) throws ParseException {

		ObjectNode response = mapper.createObjectNode();

		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;

		Hashtable<Long, Integer> errors = queryService.requestErrorCount(startTime, endTime);

		JsonNode node = mapper.valueToTree(errors);
		response.put("status","success");
		response.set("data",node);

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/count/failed/client")
	public ResponseEntity<Object> getRequestErrorCountsPerClient (@RequestBody Map<String, Object> payload) throws ParseException {
		// TODO: Parse JSON

		ObjectNode response = mapper.createObjectNode();

		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;

		Hashtable<Long,Hashtable<Long, Integer>> errors = queryService.RequestErrorCountsPerClient(startTime, endTime);

		JsonNode node = mapper.valueToTree(errors);
		response.put("status","success");
		response.set("data",node);

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}


}