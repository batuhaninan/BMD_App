package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
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
	public ResponseEntity<Object> getRequestCount (@RequestBody Map<String, Object> payload) throws ParseException {
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));
		Date endTime = formatter.parse((String) payload.get("endTime"));
		ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
		Hashtable<Long, Integer> requestsByClient = new Hashtable<Long, Integer>();
		for (Delivery delivery : deliveries){
			if (delivery.getResultCode() == -1){
				continue;
			}
			if (delivery.getRequest().getStartTime().after(startTime)){
				if (endTime.after(delivery.getRequest().getEndTime())){
					if (requestsByClient.get(delivery.getRequest().getClient().getId())!=null){
						requestsByClient.put(delivery.getRequest().getClient().getId(), requestsByClient.get(delivery.getRequest().getClient().getId())+1);
					}
					else{
						requestsByClient.put(delivery.getRequest().getClient().getId(),1);
					}
				}
			}
		}
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
		int success = 0;
		int failed = 0;
		ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
		for (Delivery delivery : deliveries){
			if (delivery.getResultCode() == -1){
				continue;
			}
			if (delivery.getRequest().getStartTime().after(startTime)){
				if (endTime.after(delivery.getRequest().getEndTime())){
					if (delivery.getResultCode() == 0 || delivery.getResultCode() == 200 ){
						success++;
					}
					else{
						failed++;
					}
				}
			}
		}
		response.put("status","success");
		response.put("successCount", success);
		response.put("failCount",failed);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/count/failed/")
	public ResponseEntity<Object> getRequestErrorCounts (@RequestBody Map<String, Object> payload) throws ParseException {
		Hashtable<Long, Integer> errors = new Hashtable<Long, Integer>();
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;
		ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
		for (Delivery delivery : deliveries){
			if (delivery.getResultCode() == -1){
				continue;
			}
			if (delivery.getRequest().getStartTime().after(startTime)){
				if (endTime.after(delivery.getRequest().getEndTime())){
					if(delivery.getResultCode()!=0){ // fix request.getSuccess
//						if (Long.valueOf(delivery.getResultCode()).equals(null)){
//							continue;
//						}
						if (errors.get(delivery.getResultCode()) != null) {
							errors.put(delivery.getResultCode(), errors.get(delivery.getResultCode()) + 1);
						}
						else{
							errors.put(delivery.getResultCode(), 1);
						}
					}
				}
			}
		}
		JsonNode node = mapper.valueToTree(errors);
		response.put("status","success");
		response.set("data",node);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping(path="/count/failed/client")
	public ResponseEntity<Object> getRequestErrorCountsPerClient (@RequestBody Map<String, Object> payload) throws ParseException {
		// TODO: Parse JSON
		Hashtable<Long,Hashtable<Long, Integer>> errors = new Hashtable<Long, Hashtable<Long, Integer>>();
		ObjectNode response = mapper.createObjectNode();
		Date startTime = formatter.parse((String) payload.get("startTime"));;
		Date endTime = formatter.parse((String) payload.get("endTime"));;
		ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
		for (Delivery delivery : deliveries){
			if (delivery.getResultCode() == -1){
				continue;
			}
			if (delivery.getRequest().getStartTime().after(startTime)){
				if (endTime.after(delivery.getRequest().getEndTime())){
					if(delivery.getResultCode()!=0){
						Hashtable<Long, Integer> innerMap = new Hashtable<Long, Integer>();
						if(errors.get(delivery.getRequest().getClient().getId())!=null){
							if ( errors.get( delivery.getRequest().getClient().getId() ).get( delivery.getResultCode() ) != null ){
								innerMap = errors.get(delivery.getRequest().getClient().getId());
								innerMap.put(delivery.getResultCode(),errors.get(delivery.getRequest().getClient().getId()).get(delivery.getResultCode())+1);
								errors.put(delivery.getRequest().getClient().getId(), innerMap);
							}
							else{
								innerMap = errors.get(delivery.getRequest().getClient().getId());
								innerMap.put(delivery.getResultCode(), 1);
								errors.put(delivery.getRequest().getClient().getId(), innerMap);
							}
						}
						else{
							innerMap.put(delivery.getResultCode(), 1);
							errors.put(delivery.getRequest().getClient().getId(), innerMap);
						}

					}
				}
			}
		}
		JsonNode node = mapper.valueToTree(errors);
		response.put("status","success");
		response.set("data",node);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}


}