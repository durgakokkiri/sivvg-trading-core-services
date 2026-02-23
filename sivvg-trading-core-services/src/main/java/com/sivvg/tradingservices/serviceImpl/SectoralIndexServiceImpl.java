package com.sivvg.tradingservices.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.SectorPerformance;
import com.sivvg.tradingservices.repository.DailyTipRepository;
import com.sivvg.tradingservices.service.SectoralIndexService;

@Service
public class SectoralIndexServiceImpl implements SectoralIndexService {

	private static final Logger logger = LoggerFactory.getLogger(SectoralIndexServiceImpl.class);

	@Autowired
	private DailyTipRepository repo;

	// 🔹 TODAY SECTOR TYPE COUNT
	@Override
	public List<SectorPerformance> getTodaySectorTypeCount() {

		LocalDate today = LocalDate.now();
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(23, 59, 59);

		logger.info("Fetching today sector type count | date={}", today);

		List<SectorPerformance> result = repo.countTodayBySectorType(start, end);

		logger.info("Sector type count fetched successfully | count={}", result.size());
		return result;
	}

	// 🔹 TODAY SECTOR TYPES (NOT IMPLEMENTED)
	@Override
	public List<SectorPerformance> getTodaySectorTypes() {

		logger.warn("getTodaySectorTypes method not implemented yet");
		return null;
	}

	// 🔹 TODAY COMPANIES BY SECTOR
	@Override
	public List<DailyTipEntity> getTodayCompaniesBySector(String sectorType) {

		LocalDate today = LocalDate.now();
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(23, 59, 59);

		logger.info("Fetching today companies by sector | sectorType={}", sectorType);

		List<DailyTipEntity> companies = repo.findTodayBySectorType(sectorType.toUpperCase(), start, end);

		logger.info("Companies fetched for sector {} | count={}", sectorType, companies.size());

		return companies;
	}
}
