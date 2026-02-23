package com.sivvg.tradingservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.DailyTipRequest;
import com.sivvg.tradingservices.service.DailyTipService;
import com.sivvg.tradingservices.service.NotificationService;
import com.sivvg.tradingservices.service.YahooFinanceClient;

@RestController
@RequestMapping("/api/v1/tips")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class DailyTipController {

	private static final Logger logger = LoggerFactory.getLogger(DailyTipController.class);

	@Autowired
	private DailyTipService service;

	@SuppressWarnings("unused")
	@Autowired
	private YahooFinanceClient yahooservice;

	@SuppressWarnings("unused")
	@Autowired
	private NotificationService notificationservice;

	// 🔹 GET ALL TIPS
	@GetMapping("/all")
	public ResponseEntity<?> getAllTips() {

		logger.info("Get All Tips API called");

		List<DailyTipEntity> tips = service.getAllTips();

		if (tips.isEmpty()) {
			logger.info("No tips available for today");
			return ResponseEntity.ok("Today no tips provided.");
		}

		logger.info("Total tips fetched: {}", tips.size());
		return ResponseEntity.ok(tips);
	}

	// 🔹 GET TIPS BY CATEGORY
	@GetMapping("/tips")
	public List<DailyTipEntity> getTips(@RequestParam(required = false) String category) {

		logger.info("Get Tips API called with category: {}", category);

		if (category == null || category.isEmpty()) {
			logger.debug("Category not provided, fetching all tips");
			return service.getAllTips();
		}

		return service.getTipsByCategory(category);
	}

	// 🔹 GET SINGLE TIP STATUS
	@GetMapping("/tip/{id}")
	public ResponseEntity<DailyTipEntity> getTipStatus(@PathVariable Long id) {

		logger.info("Get Tip Status API called for ID: {}", id);

		DailyTipEntity tip = service.getTipStatus(id);

		logger.info("Tip status fetched successfully for ID: {}", id);
		return ResponseEntity.ok(tip);
	}

	// 🔹 UPDATE TIP
	@PutMapping("/update/{id}")
	public ResponseEntity<DailyTipEntity> updateTip(@PathVariable Long id, @RequestBody DailyTipRequest req) {

		logger.info("Update Tip API called for ID: {}", id);
		logger.debug("Update Tip Request: {}", req);

		DailyTipEntity updatedTip = service.updateTip(id, req);

		logger.info("Tip updated successfully for ID: {}", id);
		return ResponseEntity.ok(updatedTip);
	}

	// 🔹 DELETE TIP
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteTip(@PathVariable Long id) {

		logger.info("Delete Tip API called for ID: {}", id);

		service.deleteTip(id);

		logger.info("Tip deleted successfully for ID: {}", id);
		return ResponseEntity.noContent().build();
	}
}
