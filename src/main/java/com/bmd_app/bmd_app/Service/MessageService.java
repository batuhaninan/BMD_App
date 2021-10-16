package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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


}
