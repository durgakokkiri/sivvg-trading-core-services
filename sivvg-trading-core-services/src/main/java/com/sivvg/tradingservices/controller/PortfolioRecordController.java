package com.sivvg.tradingservices.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
import com.sivvg.tradingservices.service.PortfolioRecordService;

@RestController
@RequestMapping("/api/v1/records")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class PortfolioRecordController {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioRecordController.class);

	private final PortfolioRecordService recordService;

	public PortfolioRecordController(PortfolioRecordService recordService) {
		this.recordService = recordService;
	}

	// ---------- DAILY RECORD ----------

	@GetMapping("/daily")
	public ResponseEntity<List<PortfolioDailyRecord>> getAllDaily() {

		logger.info("Get all daily portfolio records API called");

		List<PortfolioDailyRecord> records = recordService.getAllDailyRecords();

		logger.info("Total daily records fetched: {}", records.size());
		return ResponseEntity.ok(records);
	}

	@GetMapping("/daily/{id}")
	public ResponseEntity<PortfolioDailyRecord> getDailyById(@PathVariable Long id) {

		logger.info("Get daily portfolio record by ID API called | id={}", id);

		return ResponseEntity.ok(recordService.getDailyRecordById(id));
	}

	@PutMapping("/daily/{id}")
	public ResponseEntity<PortfolioDailyRecord> updateDaily(@PathVariable Long id,
			@RequestBody PortfolioDailyRecord record) {

		logger.info("Update daily portfolio record API called | id={}", id);

		return ResponseEntity.ok(recordService.updateDailyRecord(id, record));
	}

	@PatchMapping("/daily/{id}")
	public ResponseEntity<PortfolioDailyRecord> patchDaily(@PathVariable Long id,
			@RequestBody Map<String, Object> updates) {

		logger.info("Patch daily portfolio record API called | id={}", id);
		logger.debug("Patch updates keys: {}", updates.keySet());

		return ResponseEntity.ok(recordService.patchDailyRecord(id, updates));
	}

	@DeleteMapping("/daily/{id}")
	public ResponseEntity<String> deleteDaily(@PathVariable Long id) {

		logger.info("Delete daily portfolio record API called | id={}", id);

		recordService.deleteDailyRecord(id);

		logger.info("Daily portfolio record deleted successfully | id={}", id);
		return ResponseEntity.ok("Daily record deleted successfully");
	}

	// ---------- MONTHLY SUMMARY ----------

	@GetMapping("/monthly")
	public ResponseEntity<List<PortfolioMonthlyUserSummary>> getMonthly() {

		logger.info("Get all monthly portfolio summaries API called");

		List<PortfolioMonthlyUserSummary> list = recordService.getAllMonthlySummaries();

		logger.info("Total monthly summaries fetched: {}", list.size());
		return ResponseEntity.ok(list);
	}

	@GetMapping("/monthly/month/{month}")
	public ResponseEntity<Object> getMonthlyByMonth(@PathVariable String month) {

		logger.info("Get monthly portfolio summary by month API called | month={}", month);

		return ResponseEntity.ok(recordService.getMonthlyByMonth(month));
	}

	@DeleteMapping("/monthly/{id}")
	public ResponseEntity<String> deleteMonthly(@PathVariable Long id) {

		logger.warn("Delete monthly portfolio summary API called | id={}", id);

		recordService.deleteMonthlySummary(id);

		logger.info("Monthly portfolio summary deleted successfully | id={}", id);
		return ResponseEntity.ok("Monthly summary deleted successfully");
	}
}
