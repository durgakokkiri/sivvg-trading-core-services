package com.sivvg.tradingservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.playload.ContactUSRequestDto;
import com.sivvg.tradingservices.service.ContactUSService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contact")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class ContactUSController {

	private static final Logger logger = LoggerFactory.getLogger(ContactUSController.class);

	private final ContactUSService contactService;

	public ContactUSController(ContactUSService contactService) {
		this.contactService = contactService;
	}

	@PostMapping
	public ResponseEntity<String> createContact(@Valid @RequestBody ContactUSRequestDto dto) {

		logger.info("Create Contact Request API called");

		// 🔑 Extract userId from JWT
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();

		logger.debug("Authenticated userId: {}", userId);
		logger.debug("Contact request payload: {}", dto);

		contactService.createContactRequest(dto, userId);

		logger.info("Contact request submitted successfully for userId: {}", userId);

		return ResponseEntity.ok("Contact request submitted successfully");
	}
}
