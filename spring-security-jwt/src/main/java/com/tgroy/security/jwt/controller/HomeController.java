package com.tgroy.security.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tgroy.security.jwt.model.AuthenticationRequest;
import com.tgroy.security.jwt.model.AuthenticationResponse;
import com.tgroy.security.jwt.util.JwtUtil;

@RestController
public class HomeController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/")
	public String home() {
		return "<h1>Welcome</h1>";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authReq) throws Exception {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(authReq.getUserName(), authReq.getPassword()));
		} catch (AuthenticationException e) {
			throw new Exception("Incorrect username or password", e);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authReq.getUserName());
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

}
