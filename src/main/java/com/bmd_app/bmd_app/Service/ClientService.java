package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;


    public Boolean removeClient(Long id){
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return false;
        }

        clientRepository.deleteById(id);
        return true;
    }

    public Optional<Client> getClientWithId(Long id){
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return null;
        }
        return client;
    }


}
