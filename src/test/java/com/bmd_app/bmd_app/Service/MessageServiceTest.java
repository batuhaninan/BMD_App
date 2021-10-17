package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Repository.DeliveryRepository;
import com.bmd_app.bmd_app.Repository.RequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;
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


        Client client = new Client();
        client.setName("Batuhan INAN");
        client.setPassword("ELMARMUT");
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(15L);
        clientRepository.save(client);



        ObjectNode response = mapper.createObjectNode();


        Long Id = client.getId();
        String senderAddress = "5313369595";
        ArrayList<String>destinationNumbers = new ArrayList<>();
        destinationNumbers.add("3210839021");
        String messageBody = "Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?";
        Date startTime = Date.valueOf("2021-10-16");
        Date endTime = Date.valueOf("2021-10-20");


        messageService.sendMessage(client.getId(),senderAddress,destinationNumbers,messageBody,startTime,endTime, response);
        System.out.println(response.get("errorMessage"));

        assertEquals("\"maximum message length can be 1024 characters\"",response.get("errorMessage").toString());

    }

    @Test
    public void shouldNotFindClient() throws ExecutionException, InterruptedException {
        clientRepository.deleteAll();


        ObjectNode response = mapper.createObjectNode();


        Long Id = 99L;
        String senderAddress = "5313369595";
        ArrayList<String>destinationNumbers = new ArrayList<>();
        destinationNumbers.add("3210839021");
        String messageBody = "Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?";
        Date startTime = Date.valueOf("2021-10-16");
        Date endTime = Date.valueOf("2021-10-20");


        messageService.sendMessage(-1L,senderAddress,destinationNumbers,messageBody,startTime,endTime, response);

        assertEquals("\"Cannot find client\"",response.get("errorMessage").toString());

    }


    @Test
    public void shouldExceedDailyQuota() throws ExecutionException, InterruptedException {
        clientRepository.deleteAll();


        Client client = new Client();
        client.setName("Batuhan INAN");
        client.setPassword("ELMARMUT");
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(9L);
        clientRepository.save(client);



        ObjectNode response = mapper.createObjectNode();


        Long Id = client.getId();
        String senderAddress = "5313369595";
        ArrayList<String>destinationNumbers = new ArrayList<>();
        for (int i =0; i<10; i++ ){
            destinationNumbers.add("32108390"+i);
        }
        String messageBody = "Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?";
        Date startTime = Date.valueOf("2021-10-16");
        Date endTime = Date.valueOf("2021-10-20");


        messageService.sendMessage(client.getId(),senderAddress,destinationNumbers,messageBody,startTime,endTime, response);


        assertEquals("\"Out of quota, delivered message count: 9\"",response.get("errorMessage").toString());

    }

    @Test
    public void shouldBeSuccess() throws ExecutionException, InterruptedException {
        clientRepository.deleteAll();


        Client client = new Client();
        client.setName("Batuhan INAN");
        client.setPassword("ELMARMUT");
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(9L);
        clientRepository.save(client);

        ObjectNode response = mapper.createObjectNode();

        Long Id = client.getId();
        String senderAddress = "5313369595";
        ArrayList<String>destinationNumbers = new ArrayList<>();
        destinationNumbers.add("3210839021");
        String messageBody = "Ben bir vaka ile karsi karsiya geldigimde kendime hemen su 13 soruyu sorarim. Kacta, hangi, ne ile, nicin, nolmus, kimi, nerede, nasil, ne zaman, kimden, neyi, ne belli, neye, kim?";
        Date startTime = Date.valueOf("2021-10-16");
        Date endTime = Date.valueOf("2021-10-20");

        messageService.sendMessage(client.getId(),senderAddress,destinationNumbers,messageBody,startTime,endTime, response);
        System.out.println(response.get("status"));
        assertEquals("\"success\"",response.get("status").toString());




    }


}