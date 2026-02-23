package com.sivvg.tradingservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.playload.Profilepayload;
import com.sivvg.tradingservices.service.Profileservice;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

	private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

	@Autowired
	private Profileservice profileservice;

	@GetMapping("/{userId}")
	public ResponseEntity<Profilepayload> getProfile(@PathVariable("userId") String userId) {

		logger.info("Get Profile API called | userId={}", userId);

		Profilepayload profile = profileservice.getProfileByUserId(userId);

		logger.info("Profile fetched successfully | userId={}", userId);
		return ResponseEntity.ok(profile);
	}
}
