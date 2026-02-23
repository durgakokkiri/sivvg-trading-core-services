package com.sivvg.tradingservices.controller;

import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.service.MarketHolidayService;

@RestController
@RequestMapping("/api/v1/holidays")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class MarketHolidayController {

	private static final Logger logger = LoggerFactory.getLogger(MarketHolidayController.class);

	@Autowired
	private MarketHolidayService marketHolidayService;

	// 🔹 CHECK TODAY HOLIDAY
	@GetMapping("/today")
	public boolean checkTodayHoliday() {

		logger.info("Check Today Market Holiday API called");

		boolean isHoliday = marketHolidayService.isMarketHoliday();

		logger.info("Today market holiday status: {}", isHoliday);
		return isHoliday;
	}

	
	// CHECK WITH / WITHOUT DATE
	@GetMapping("/check")
	public boolean checkHoliday(@RequestParam(required = false) String date) {

		logger.info("Check Market Holiday API called with date: {}", date);

		LocalDate d;

		if (date == null || date.isBlank()) {
			d = LocalDate.now(ZoneId.of("Asia/Kolkata"));
			logger.debug("Date not provided, using current date: {}", d);
		} else {
			d = LocalDate.parse(date);
			logger.debug("Parsed date from request: {}", d);
		}

		boolean isHoliday = marketHolidayService.isMarketHoliday(d);

		logger.info("Market holiday status for {} : {}", d, isHoliday);
		return isHoliday;
	}
}
