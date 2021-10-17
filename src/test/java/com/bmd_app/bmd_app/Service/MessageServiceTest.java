package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldExceedMaxCharacterCount() throws ExecutionException, InterruptedException {
        clientRepository.deleteAll();
        requestRepository.deleteAll();
        deliveryRepository.deleteAll();

        Client client = new Client();
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(15L);
        client.setPassword("ELMARMUT");
        client.setName("Batuhan INAN");

        clientRepository.save(client);

        HashMap<String, Object> postRequestJSON = new HashMap<>();

        ObjectNode response = mapper.createObjectNode();

        Long Id = client.getId();
        String senderAddress = "5313369595";
        ArrayList<String>destinationNumbers = new ArrayList<>();
        destinationNumbers.add("3210839021");
        String messageBody = "Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?";
        Date startTime = Date.valueOf("2021-10-16");
        Date endTime = Date.valueOf("19-10-2021");


        messageService.sendMessage(Id,senderAddress,destinationNumbers,messageBody,startTime,endTime, response);
        System.out.println(response.get("status"));


    }

}