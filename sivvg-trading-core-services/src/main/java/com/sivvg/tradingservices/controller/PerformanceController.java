package com.sivvg.tradingservices.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.model.DailyTipHistoryEntity;
import com.sivvg.tradingservices.playload.CategoryPerformance;
import com.sivvg.tradingservices.repository.DailyTipHistoryRepository;
import com.sivvg.tradingservices.service.PerformanceService;

@RestController
@RequestMapping("/api/v1/pastperformance")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class PerformanceController {

	private static final Logger logger = LoggerFactory.getLogger(PerformanceController.class);

	@Autowired
	private PerformanceService service;

	@Autowired
	private DailyTipHistoryRepository repo;

	// 🔹 GET HISTORICAL DATA (FILTERS OPTIONAL)
	@GetMapping("all")
	public ResponseEntity<List<DailyTipHistoryEntity>> getHistoricalData(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

		logger.info("Get Historical Data API called | category={}, from={}, to={}", category, from, to);

		// Category + Date
		if (category != null && from != null && to != null) {

			LocalDateTime start = from.atStartOfDay();
			LocalDateTime end = to.atTime(23, 59, 59);

			logger.debug("Fetching history by category and date range");
			return ResponseEntity.ok(repo.findByCategoryAndCreatedAtBetween(category.toUpperCase(), start, end));
		}

		// Only Date
		if (from != null && to != null) {

			LocalDateTime start = from.atStartOfDay();
			LocalDateTime end = to.atTime(23, 59, 59);

			logger.debug("Fetching history by date range only");
			return ResponseEntity.ok(repo.findByCreatedAtBetween(start, end));
		}

		// Only Category
		if (category != null) {

			logger.debug("Fetching history by category only");
			return ResponseEntity.ok(repo.findByCategory(category.toUpperCase()));
		}

		// No filter
		logger.debug("Fetching all historical data (no filters)");
		return ResponseEntity.ok(repo.findAll());
	}

	// 🔹 CATEGORY PERFORMANCE (AUTO PERIOD)
	@GetMapping("/category")
	public CategoryPerformance getCategoryPerformance(@RequestParam String category) {

		logger.info("Get Category Performance API called | category={}", category);

		CategoryPerformance performance = service.getCategoryPerformance(category);

		logger.info("Category performance calculated successfully | category={}", category);

		return performance;
	}

	// 🔹 1 DAY PERFORMANCE
	// /api/performance/1d?category=EQUITY
	@GetMapping("/1d")
	public List<DailyTipHistoryEntity> getTodayTips(@RequestParam String category) {

		logger.info("Get 1D Performance API called | category={}", category);

		return service.getTips1D(category);
	}

	// 🔹 1 WEEK PERFORMANCE
	// /api/performance/1w?category=EQUITY
	@GetMapping("/1w")
	public List<DailyTipHistoryEntity> getWeeklyTips(@RequestParam String category) {

		logger.info("Get 1W Performance API called | category={}", category);

		return service.getTips1W(category);
	}

	// 🔹 1 MONTH PERFORMANCE
	// /api/performance/1m?category=EQUITY
	@GetMapping("/1m")
	public List<DailyTipHistoryEntity> getMonthlyTips(@RequestParam String category) {

		logger.info("Get 1M Performance API called | category={}", category);

		return service.getTips1M(category);
	}

	// 🔹 CUSTOM DATE RANGE
	// /api/performance/by-date?category=EQUITY&from=2026-01-01&to=2026-01-21
	@GetMapping("/by-date")
	public List<DailyTipHistoryEntity> getTipsByDateRange(@RequestParam String category,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

		logger.info("Get Performance By Date Range API called | category={}, from={}, to={}", category, from, to);

		return service.getTipsByDateRange(category, from, to);
	}
}
