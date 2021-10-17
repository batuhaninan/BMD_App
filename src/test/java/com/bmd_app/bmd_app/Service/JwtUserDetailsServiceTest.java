package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JwtUserDetailsServiceTest {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void shouldFindUserByEmail(){
        clientRepository.deleteAll();

        Client client = new Client();
        client.setEmail("inanbatuhan61@gmail.com");
        client.setDailyMessageQuota(15L);
        client.setPassword("ELMARMUT");
        client.setName("Batuhan INAN");

        clientRepository.save(client);

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(client.getEmail());
        assertEquals(userDetails.getUsername(), client.getEmail());
        assertEquals(userDetails.getPassword(), client.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    public void shouldNotFindUserByEmail(){
        clientRepository.deleteAll();

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            jwtUserDetailsService.loadUserByUsername("atilla");
        });

        String expectedMessage = "Client not found with email: atilla";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));



    }

}