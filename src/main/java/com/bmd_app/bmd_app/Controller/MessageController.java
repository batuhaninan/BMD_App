package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.bmd_app.bmd_app.Service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(path="/api/message")
public class MessageController {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	MessageService messageService = new MessageService();

	@Autowired
	ObjectMapper mapper;

	SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);


	@PostMapping(path="/")
	public @ResponseBody
	Object sendMessage (@RequestBody Map<String, Object> payload) throws ParseException, ExecutionException, InterruptedException {
		ObjectNode response = mapper.createObjectNode();

		Long clientId = Long.valueOf((Integer) payload.get("clientId"));
		String senderAddress = (String) payload.get("senderAddress");
		String messageBody = (String) payload.get("messageBody");
		Date startTime = formatter.parse((String) payload.get("startTime"));
		Date endTime = formatter.parse((String) payload.get("endTime"));

		ArrayList<Delivery> destinationNumbers = new ArrayList<Delivery>();
		Optional<Client> client = clientRepository.findById(clientId);

		ArrayList<Delivery> deliveries = deliveryRepository.findAllBySingleId(clientId);

		if (client.isEmpty()) {
			response.put("status", "failed");
			response.put("errorMessage", "Cannot find client");

			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}

		Long messageLeft = client.get().getDailyMessageQuota();

		for (Delivery delivery : deliveries){
			Date deliveryTime = delivery.getRequest().getStartTime();
			if (delivery.getRequest().getStartTime().compareTo(startTime) == 0){
				messageLeft--;
			}
		}

		if (messageBody.length() > 1024){
			response.put("status","failed");
			response.put("errorMessage","maximum message length can be 1024 characters");

			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}


		Request request = new Request();
		request.setClient(client.get());
		request.setSenderAddress(senderAddress);
		request.setMessageBody(messageBody);
		request.setStartTime(startTime);
		request.setEndTime(endTime);

		boolean flag = false;

		Integer counter = 0;
		for (Object destinationNumber : (ArrayList) payload.get("destinationNumbers")) {
			if (messageLeft<=0){
				flag = true;
				break;
			}
			Delivery delivery = new Delivery();
			delivery.setDestinationNumber((String) destinationNumber);
			delivery.setSuccess(false);
			delivery.setRequest(request);
			delivery.setCancelled(false);
			delivery.setResultCode(Long.valueOf(-1));

			destinationNumbers.add(delivery);
			messageLeft--;
			counter++;
		}

		for (Delivery delivery : destinationNumbers) {
			if (delivery.getCancelled()){
				continue;
			}

			Long resultCode = Long.valueOf(messageService.call(request, delivery).get());


			delivery.setResultCode(resultCode);


			delivery.setSuccess(resultCode <= 0);
		}

		requestRepository.save(request);
		deliveryRepository.saveAll(destinationNumbers);

		response.put("clientId", client.get().getId());
		response.put("requestId", request.getId());

		if (flag){

			response.put("status", "failed");
			response.put("errorMessage","Out of quota, delivered message count: " + counter);

			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}

		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@PutMapping(path="/cancel")
	public ResponseEntity<Object> cancelDelivery (@RequestBody Map<String, Object> payload) {

		ObjectNode response = mapper.createObjectNode();

		Boolean requestFlag = false;
		Boolean destinationFlag = false;

		Long requestId = Long.valueOf((Integer) payload.get("requestId"));
		String destinationNumber = (String) payload.get("destinationNumber");

		ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();

		for (Delivery delivery : deliveries){
			if (Objects.equals(delivery.getRequest().getId(), requestId)){
				requestFlag = true;
				if (Objects.equals(delivery.getDestinationNumber(), destinationNumber)){
					destinationFlag = true;
					delivery.setCancelled(true);
					deliveryRepository.save(delivery);
				}
			}
		}

		if (!requestFlag){
			response.put("errorMessage", "Invalid Parameter : requestId");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}
		if (!destinationFlag){
			response.put("errorMessage", "Invalid Parameter : destinationNumber");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}
		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
