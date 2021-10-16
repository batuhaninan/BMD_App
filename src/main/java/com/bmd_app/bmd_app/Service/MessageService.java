package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Delivery;
import com.bmd_app.bmd_app.Entity.Request;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	MessageServiceCenter messageServiceCenter = new MessageServiceCenter();

	public Integer call (Request request, Delivery delivery) {

		String senderAddress = request.getSenderAddress();
		String destinationNumber = delivery.getDestinationNumber();
		String messageBody = request.getMessageBody();

		return messageServiceCenter.submitMessage(senderAddress, destinationNumber, messageBody);
	}


}
