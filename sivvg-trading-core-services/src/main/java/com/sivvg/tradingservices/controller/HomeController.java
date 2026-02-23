package com.sivvg.tradingservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@GetMapping("/home")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<String> home() {

		logger.info("Home API called");

		String response = "Welcome to SIVVG Home - authenticated!";

		logger.info("Home API response sent successfully");
		return ResponseEntity.ok(response);
	}
}
