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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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
	MessageService messageService;

	@Autowired
	ObjectMapper mapper;

	SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);


	@PostMapping(path="/")
	public @ResponseBody
	ObjectNode sendMessage (@RequestBody Map<String, Object> payload) throws ParseException {
		ObjectNode response = mapper.createObjectNode();
		Integer clientId = (Integer) payload.get("clientId");
		String senderAddress = (String) payload.get("senderAddress");
		String messageBody = (String) payload.get("messageBody");
		Date startTime = formatter.parse((String) payload.get("startTime"));
		Date endTime = formatter.parse((String) payload.get("endTime"));

		ArrayList<Delivery> destinationNumbers = new ArrayList<Delivery>();
		Optional<Client> client = clientRepository.findById((int) clientId.longValue());

		if (client.isEmpty()) {
			response.put("status", "failed");
			response.put("errorMessage", "Cannot find client");

			return response;
		}

		Request request = new Request();
		request.setClient(client.get());
		request.setSenderAddress(senderAddress);
		request.setMessageBody(messageBody);
		request.setStartTime(startTime);
		request.setEndTime(endTime);
		request.setResultCode(-1);

		for (Object destinationNumber : (ArrayList) payload.get("destinationNumbers")) {
			Delivery delivery = new Delivery();
			delivery.setDestinationNumber((String) destinationNumber);
			delivery.setSuccess(false);
			delivery.setRequest(request);

			destinationNumbers.add(delivery);
		}
		request.setDestinationNumbers(destinationNumbers);

		for (Delivery delivery : request.getDestinationNumbers()) {
			Integer resultCode = messageService.call(request, delivery);

			request.setResultCode(resultCode);

			delivery.setSuccess(resultCode <= 0);
			deliveryRepository.save(delivery);
		}

		requestRepository.save(request);

		response.put("status", "success");
		response.put("clientId", client.get().getId());
		response.put("requestId", request.getId());
		return response;
	}


}
