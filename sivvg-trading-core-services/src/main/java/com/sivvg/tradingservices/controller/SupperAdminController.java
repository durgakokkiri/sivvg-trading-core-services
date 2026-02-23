package com.sivvg.tradingservices.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.playload.AdminCreateRequest;
import com.sivvg.tradingservices.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
public class SupperAdminController {

	private static final Logger logger = LoggerFactory.getLogger(SupperAdminController.class);

	@Autowired
	private AdminService adminService;

	// CREATE ADMIN
	@PostMapping("/create-admin")
	public ResponseEntity<?> createAdmin(@Valid @RequestBody AdminCreateRequest request, BindingResult bindingResult) {

		logger.info("Create Admin API called");

		// ---------- VALIDATION ERRORS ----------
		if (bindingResult.hasErrors()) {

			Map<String, String> errors = new HashMap<>();

			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}

			logger.warn("Admin creation validation failed: {}", errors);

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}

		// ---------- BUSINESS LOGIC ----------
		adminService.createAdmin(request);

		logger.info("Admin created successfully | email={}", request.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
	}
}
