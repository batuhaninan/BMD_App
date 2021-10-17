package com.bmd_app.bmd_app.Controller;

import com.bmd_app.bmd_app.Entity.Client;
import com.bmd_app.bmd_app.Jwt.JwtTokenUtil;
import com.bmd_app.bmd_app.Repository.ClientRepository;
import com.bmd_app.bmd_app.Service.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping(path="/api/auth")
public class AuthController {

	@Autowired
	ClientRepository clientRepository;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PostMapping(path="/login")
	public ResponseEntity<Object> login (@RequestBody Map<String, Object> payload) throws Exception {
		ObjectNode response = mapper.createObjectNode();

		String email = (String) payload.get("email");
		String password = (String) payload.get("password");

		final String hashedPassword = Hashing.sha256()
						.hashString(password, StandardCharsets.US_ASCII)
						.toString();


		final UserDetails userDetails = userDetailsService
						.loadUserByUsername(email);

		final String token = jwtTokenUtil.generateToken(userDetails);
		response.put("token", token);


		response.put("status", "success");
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@PostMapping(path="/logout")
	public ResponseEntity<Object> logout (@RequestBody Map<String, Object> payload){
		ObjectNode response = mapper.createObjectNode();

		String email = (String) payload.get("email");
		String password = (String) payload.get("password");
		String token = (String) payload.get("token");

		final String hashedPassword = Hashing.sha256()
						.hashString(password, StandardCharsets.US_ASCII)
						.toString();

		Optional<Client> client = clientRepository.findByEmail(email);

		if (client.isEmpty()) {
			response.put("status", "failed");
			response.put("errorMessage", "Incorrect email");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}

		if (!Objects.equals(client.get().getPassword(), hashedPassword)) {
			response.put("status", "failed");
			response.put("errorMessage", "Incorrect password");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}

		clientRepository.save(client.get());
		final UserDetails userDetails = userDetailsService
						.loadUserByUsername(email);
		if (!jwtTokenUtil.validateToken(token, userDetails)) {

			token = jwtTokenUtil.generateToken(userDetails);
			response.put("token", token);
		}

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@PostMapping(path="/signup")
	public ResponseEntity<Object> signUp (@RequestBody Map<String, Object> payload) throws Exception {
		ObjectNode response = mapper.createObjectNode();

		String name = (String) payload.get("name");
		String password = (String) payload.get("password");
		String email = (String) payload.get("email");
		Long dailyMessageQuota = Long.valueOf((Integer) payload.get("dailyMessageQuota"));

		final String hashedPassword = Hashing.sha256()
						.hashString(password, StandardCharsets.US_ASCII)
						.toString();


		Client client = new Client();
		client.setName(name);
		client.setPassword(hashedPassword);
		client.setEmail(email);
		client.setDailyMessageQuota(dailyMessageQuota);
		clientRepository.save(client);

		final UserDetails userDetails = userDetailsService
						.loadUserByUsername(email);
		final String token = jwtTokenUtil.generateToken(userDetails);


		response.put("status", "success");
		response.put("token", token);
		response.put("id",client.getId());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}


	private void authenticate(String email, String password) throws Exception {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
		try {
			authenticationManager.authenticate(token);
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}

