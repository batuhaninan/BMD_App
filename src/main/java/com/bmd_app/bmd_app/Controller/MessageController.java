package com.bmd_app.bmd_app.Controller;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(path="/api/message")
public class MessageController {

	@Autowired
	private MessageController messageController;

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

	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);


	@PostMapping(path="/")
	public @ResponseBody
	Object sendMessage (@RequestBody Map<String, Object> payload) throws ParseException, ExecutionException, InterruptedException {
		ObjectNode response = mapper.createObjectNode();

		Long clientId = Long.valueOf((Integer) payload.get("clientId"));
		String senderAddress = (String) payload.get("senderAddress");
		String messageBody = (String) payload.get("messageBody");
		Date startTime = formatter.parse((String) payload.get("startTime"));
		Date endTime = formatter.parse((String) payload.get("endTime"));
		ArrayList destinationNumbers = (ArrayList) payload.get("destinationNumbers");

		if (destinationNumbers.size() > 10) {

			response.put("status", "failed");
			response.put("errorMessage", "Too many destination numbers (max 10)");

			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		Integer result = messageService.sendMessage(clientId, senderAddress, destinationNumbers, messageBody, startTime, endTime, response);

		if (result == 1) {
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@PutMapping(path="/cancel")
	public ResponseEntity<Object> cancelDelivery (@RequestBody Map<String, Object> payload) {

		ObjectNode response = mapper.createObjectNode();

		Long requestId = Long.valueOf((Integer) payload.get("requestId"));
		String destinationNumber = (String) payload.get("destinationNumber");

		Integer result = messageService.cancelMessage(requestId, destinationNumber);

		if (result == 1){
			response.put("errorMessage", "Invalid Parameter : requestId");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		if (result == 2){
			response.put("errorMessage", "Invalid Parameter : destinationNumber");
			return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
