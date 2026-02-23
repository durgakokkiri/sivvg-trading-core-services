package com.sivvg.tradingservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.SectorPerformance;
import com.sivvg.tradingservices.service.SectoralIndexService;

@RestController
@RequestMapping("/api/v1/sectors")
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class SectoralIndexController {

	private static final Logger logger = LoggerFactory.getLogger(SectoralIndexController.class);

	@Autowired
	private SectoralIndexService service;

	// 🔹 SIMPLE & SAFE URL (no class-level mapping)
	@GetMapping("/type-count")
	public List<SectorPerformance> getTodaySectorTypeCount() {

		logger.info("Get Today Sector Type Count API called");

		List<SectorPerformance> result = service.getTodaySectorTypeCount();

		logger.info("Sector type count fetched successfully | count={}", result.size());
		return result;
	}

	// Example: /api/sectors/companies?sectorType=PHARMA
	@GetMapping("/companies")
	public List<DailyTipEntity> getTodayCompaniesBySector(@RequestParam String sectorType) {

		logger.info("Get Today Companies By Sector API called | sectorType={}", sectorType);

		List<DailyTipEntity> companies = service.getTodayCompaniesBySector(sectorType);

		logger.info("Companies fetched for sector {} | count={}", sectorType, companies.size());

		return companies;
	}
}
