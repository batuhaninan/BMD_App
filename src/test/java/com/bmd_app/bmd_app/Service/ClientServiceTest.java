package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Test
    public void shouldRemoveClient() throws Exception {

        clientRepository.deleteAll();

        Client client = new Client();
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(15L);
        client.setPassword("ELMARMUT");
        client.setName("Batuhan INAN");

        clientRepository.save(client);

        clientService.removeClient(client.getId());

        Optional<Client> removedClient = clientRepository.findByEmail("inanbatuhan61@gmail.com");

        assertTrue(removedClient.isEmpty());
    }

    @Test
    public void shouldNotRemoveInvalidId() {
        assertFalse(clientService.removeClient((long) -5));
    }


    @Test
    public void shouldFindClientById() {

        clientRepository.deleteAll();

        Client client = new Client();
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(15L);
        client.setPassword("ELMARMUT");
        client.setName("Batuhan INAN");

        clientRepository.save(client);

        clientService.getClientWithId(client.getId());

        Optional<Client> foundClient = clientRepository.findByEmail("inanbatuhan61@gmail.com");

        assertTrue(foundClient.isPresent());
    }




    @Test
    public void shouldNotFindInvalidClientById() {
        assertEquals(clientService.getClientWithId((long)-5), null);
    }



}