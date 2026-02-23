package com.sivvg.tradingservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.model.DeviceTokenEntity;
import com.sivvg.tradingservices.repository.DeviceTokenRepository;

@RestController
@RequestMapping("/api/v1/device-token")
@CrossOrigin("*")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
public class DeviceTokenController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceTokenController.class);

	@Autowired
	private DeviceTokenRepository repo;

	@PostMapping
	public void save(@RequestBody DeviceTokenEntity token) {

		logger.info("Save Device Token API called");

		if (token == null || token.getToken() == null) {
			logger.warn("Received null or empty device token");
			return;
		}

		repo.findByToken(token.getToken()).ifPresentOrElse(existing -> logger.info("Device token already exists"),
				() -> {
					repo.save(token);
					logger.info("New device token saved successfully");
				});
	}
}
