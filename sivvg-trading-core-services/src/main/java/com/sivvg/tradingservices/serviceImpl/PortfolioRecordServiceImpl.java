package com.sivvg.tradingservices.serviceImpl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
import com.sivvg.tradingservices.repository.PortfolioDailyRecordRepository;
import com.sivvg.tradingservices.repository.PortfolioUserMonthlySummaryRepository;
import com.sivvg.tradingservices.service.PortfolioRecordService;

@Service
public class PortfolioRecordServiceImpl implements PortfolioRecordService {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioRecordServiceImpl.class);

	private final PortfolioDailyRecordRepository dailyRepo;
	private final PortfolioUserMonthlySummaryRepository monthlyRepo;

	public PortfolioRecordServiceImpl(PortfolioDailyRecordRepository dailyRepo,
			PortfolioUserMonthlySummaryRepository monthlyRepo) {
		this.dailyRepo = dailyRepo;
		this.monthlyRepo = monthlyRepo;
	}

	// ---------- DAILY RECORD ----------

	@Override
	public List<PortfolioDailyRecord> getAllDailyRecords() {

		logger.info("Fetching all daily portfolio records");

		List<PortfolioDailyRecord> records = dailyRepo.findAll();

		logger.info("Total daily portfolio records fetched: {}", records.size());
		return records;
	}

	@Override
	public PortfolioDailyRecord getDailyRecordById(Long id) {

		logger.info("Fetching daily portfolio record | id={}", id);

		return dailyRepo.findById(id).orElseThrow(() -> {
			logger.error("Daily portfolio record not found | id={}", id);
			return new RuntimeException("Daily record not found");
		});
	}

	@Override
	public PortfolioDailyRecord updateDailyRecord(Long id, PortfolioDailyRecord record) {

		logger.info("Updating daily portfolio record | id={}", id);

		PortfolioDailyRecord existing = getDailyRecordById(id);

		existing.setUsername(record.getUsername());
		existing.setTradeDate(record.getTradeDate());
		existing.setMonth(record.getMonth());
		existing.setDayNumber(record.getDayNumber());
		existing.setPnl(record.getPnl());
		existing.setPnlPercentage(record.getPnlPercentage());
		existing.setSector(record.getSector());

		PortfolioDailyRecord updated = dailyRepo.save(existing);

		logger.info("Daily portfolio record updated successfully | id={}", id);
		return updated;
	}

	@Override
	public PortfolioDailyRecord patchDailyRecord(Long id, Map<String, Object> updates) {

		logger.info("Patching daily portfolio record | id={}", id);
		logger.debug("Patch keys received: {}", updates.keySet());

		PortfolioDailyRecord record = getDailyRecordById(id);

		if (updates.containsKey("username"))
			record.setUsername((String) updates.get("username"));

		if (updates.containsKey("tradeDate"))
			record.setTradeDate((String) updates.get("tradeDate"));

		if (updates.containsKey("month"))
			record.setMonth((String) updates.get("month"));

		if (updates.containsKey("dayNumber"))
			record.setDayNumber((Integer) updates.get("dayNumber"));

		if (updates.containsKey("pnl"))
			record.setPnl((Integer) updates.get("pnl"));

		if (updates.containsKey("pnlPercentage"))
			record.setPnlPercentage(Double.parseDouble(updates.get("pnlPercentage").toString()));

		if (updates.containsKey("sector"))
			record.setSector((String) updates.get("sector"));

		PortfolioDailyRecord patched = dailyRepo.save(record);

		logger.info("Daily portfolio record patched successfully | id={}", id);
		return patched;
	}

	@Override
	public void deleteDailyRecord(Long id) {

		logger.warn("Deleting daily portfolio record | id={}", id);

		dailyRepo.deleteById(id);

		logger.info("Daily portfolio record deleted successfully | id={}", id);
	}

	// ---------- MONTHLY SUMMARY ----------

	@Override
	public List<PortfolioMonthlyUserSummary> getAllMonthlySummaries() {

		logger.info("Fetching all monthly portfolio summaries");

		List<PortfolioMonthlyUserSummary> summaries = monthlyRepo.findAll();

		logger.info("Total monthly portfolio summaries fetched: {}", summaries.size());
		return summaries;
	}

	@Override
	public void deleteMonthlySummary(Long id) {

		logger.warn("Deleting monthly portfolio summary | id={}", id);

		monthlyRepo.deleteById(id);

		logger.info("Monthly portfolio summary deleted successfully | id={}", id);
	}

	@Override
	public Object getMonthlyByMonth(String month) {

		logger.info("Fetching monthly portfolio summary by month | month={}", month);

		// TODO: implement logic
		logger.warn("getMonthlyByMonth not implemented yet | month={}", month);
		return null;
	}
}
