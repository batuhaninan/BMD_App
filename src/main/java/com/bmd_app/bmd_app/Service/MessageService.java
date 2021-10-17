package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
//@EnableScheduling
public class MessageService {

	MessageServiceCenter messageServiceCenter = new MessageServiceCenter();

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	DeliveryRepository deliveryRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	ObjectMapper mapper;

	@Async
	public CompletableFuture<Integer> call (Request request, Delivery delivery) {

		String senderAddress = request.getSenderAddress();
		String destinationNumber = delivery.getDestinationNumber();
		String messageBody = request.getMessageBody();

		return CompletableFuture.completedFuture(messageServiceCenter.submitMessage(senderAddress, destinationNumber, messageBody));
	}


	public Integer cancelMessage(Long requestId, String destinationNumber){
		Boolean requestFlag = false;
		Boolean destinationFlag = false;

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
			return 1;
		}
		if (!destinationFlag){
			return 2;
		}
		return 0;
	}

	public Integer sendMessage(Long clientId, String senderAddress, ArrayList requestDestinationNumbers, String messageBody, Date startTime, Date endTime, ObjectNode response) throws InterruptedException, ExecutionException {
		ArrayList<Delivery> destinationNumbers = new ArrayList<Delivery>();
		Optional<Client> client = clientRepository.findById(clientId);

		ArrayList<Delivery> deliveries = deliveryRepository.findAllBySingleId(clientId);

		if (client.isEmpty()) {
			response.put("status", "failed");
			response.put("errorMessage", "Cannot find client");

			return 1;
		}

		Long messageLeft = client.get().getDailyMessageQuota();

		for (Delivery delivery : deliveries){
			if (delivery.getRequest().getStartTime().compareTo(startTime) == 0){
				messageLeft--;
			}
		}

		if (messageBody.length() > 1024){
			response.put("status","failed");
			response.put("errorMessage","maximum message length can be 1024 characters");

			return 1;
		}


		Request request = new Request();
		request.setClient(client.get());
		request.setSenderAddress(senderAddress);
		request.setMessageBody(messageBody);
		request.setStartTime(startTime);
		request.setEndTime(endTime);

		boolean flag = false;

		Integer counter = 0;
		for (Object destinationNumber : requestDestinationNumbers) {
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
			if (delivery.getCancelled()) {
				continue;
			}

			Long resultCode = null;

			for (int i = 0; i < 3; i++) {

				resultCode = Long.valueOf(call(request, delivery).get());
				response.put(String.valueOf(i),resultCode);
				if (resultCode == 0) {
					break;
				}

				if (i != 2) {
					TimeUnit.SECONDS.sleep(1);
				}
			}


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

			return 1;
		}
		response.put("status", "success");
		return 0;
	}

	/*@Scheduled(fixedRate = 30000)
	public void scheduledMessageJob() throws ExecutionException, InterruptedException {
			ObjectNode response = mapper.createObjectNode();

			ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();

			for (Delivery delivery : deliveries) {

				if (!delivery.getSuccess() && delivery.getRequest().getStartTime().before(new Date()) && delivery.getRequest().getEndTime().after(new Date())) {
					Request request = delivery.getRequest();
					Client client = request.getClient();
					ArrayList arrayList = new ArrayList();
					arrayList.add(delivery);
					sendMessage(client.getId(), request.getSenderAddress(), arrayList, request.getMessageBody(), request.getStartTime(), request.getEndTime(), response);
				}
			}

	}*/


}
