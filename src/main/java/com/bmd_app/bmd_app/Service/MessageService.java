package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class MessageService {

	MessageServiceCenter messageServiceCenter = new MessageServiceCenter();

	@Async
	public CompletableFuture<Integer> call (Request request, Delivery delivery) {



		String senderAddress = request.getSenderAddress();
		String destinationNumber = delivery.getDestinationNumber();
		String messageBody = request.getMessageBody();

		return CompletableFuture.completedFuture(messageServiceCenter.submitMessage(senderAddress, destinationNumber, messageBody));
	}

	@Autowired
	DeliveryRepository deliveryRepository;

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
}
