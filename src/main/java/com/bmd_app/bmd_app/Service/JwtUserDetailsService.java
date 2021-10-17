package com.bmd_app.bmd_app.Service;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<Client> client = clientRepository.findByEmail(email);

		if (client.isPresent() && Objects.equals(client.get().getEmail(), email)) {
			return new User(client.get().getEmail(), client.get().getPassword(),
							new ArrayList<>());
		}
		throw new UsernameNotFoundException("Client not found with email: " + email);
	}
}
