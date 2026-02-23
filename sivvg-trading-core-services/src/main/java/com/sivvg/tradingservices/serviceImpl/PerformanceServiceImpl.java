package com.sivvg.tradingservices.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.DailyTipHistoryEntity;
import com.sivvg.tradingservices.playload.CategoryPerformance;
import com.sivvg.tradingservices.playload.PeriodStatdto;
import com.sivvg.tradingservices.repository.DailyTipHistoryRepository;
import com.sivvg.tradingservices.service.PerformanceService;

@Service
public class PerformanceServiceImpl implements PerformanceService {

	private static final Logger logger = LoggerFactory.getLogger(PerformanceServiceImpl.class);

	@Autowired
	private DailyTipHistoryRepository repo;

	// 🔹 CATEGORY PERFORMANCE (AUTO PERIODS)
	@Override
	public CategoryPerformance getCategoryPerformance(String category) {

		logger.info("Get category performance service started | category={}", category);

		LocalDate today = LocalDate.now();

		List<PeriodStatdto> stats = List.of(buildStat(category, "1D", today, today),
				buildStat(category, "1W", today.minusDays(7), today),
				buildStat(category, "1M", today.minusDays(30), today));

		logger.info("Category performance calculated successfully | category={}", category);
		return new CategoryPerformance(category.toUpperCase(), stats);
	}

	// 🔹 BUILD PERIOD STAT
	private PeriodStatdto buildStat(String category, String period, LocalDate from, LocalDate to) {

		logger.debug("Building performance stat | category={}, period={}, from={}, to={}", category, period, from, to);

		LocalDateTime start = from.atStartOfDay();
		LocalDateTime end = to.atTime(23, 59, 59);

		List<DailyTipHistoryEntity> tips = repo.findByCategoryAndCreatedAtBetween(category.toUpperCase(), start, end);

		int total = tips.size();
		int success = 0;

		for (DailyTipHistoryEntity t : tips) {
			if (Boolean.TRUE.equals(t.getT1Reached()) || Boolean.TRUE.equals(t.getT2Reached())
					|| Boolean.TRUE.equals(t.getT3Reached()) || Boolean.TRUE.equals(t.getTargetReached())) {
				success++;
			}
		}

		double rate = total == 0 ? 0 : (success * 100.0 / total);

		logger.debug("Period stat built | period={}, total={}, success={}, successRate={}", period, total, success,
				rate);

		return new PeriodStatdto(period, total, success, rate);
	}

	// 🔹 COMMON SUCCESS LOGIC (UNUSED NOW, KEPT SAFE)
	@SuppressWarnings("unused")
	private CategoryPerformance buildCategoryPerformance(String category, String period,
			List<DailyTipHistoryEntity> tips) {

		logger.debug("Building category performance | category={}, period={}", category, period);

		int total = tips.size();
		int success = 0;

		for (DailyTipHistoryEntity t : tips) {
			if (t.getT1Reached() || t.getT2Reached() || t.getT3Reached()) {
				success++;
			}
		}

		double rate = (total == 0) ? 0 : (success * 100.0 / total);

		PeriodStatdto stat = new PeriodStatdto(period, total, success, rate);

		return new CategoryPerformance(category, List.of(stat));
	}

	// 🔹 COMMON DATE RANGE QUERY
	private List<DailyTipHistoryEntity> getTipsBetween(String category, LocalDate from, LocalDate to) {

		logger.debug("Fetching tips between dates | category={}, from={}, to={}", category, from, to);

		LocalDateTime start = from.atStartOfDay();
		LocalDateTime end = to.atTime(23, 59, 59);

		return repo.findByCategoryAndCreatedAtBetween(category.toUpperCase(), start, end);
	}

	// 🔹 TODAY (1D)
	@Override
	public List<DailyTipHistoryEntity> getTips1D(String category) {

		logger.info("Fetching 1D tips | category={}", category);

		LocalDate today = LocalDate.now();
		return getTipsBetween(category, today, today);
	}

	// 🔹 WEEK (1W)
	@Override
	public List<DailyTipHistoryEntity> getTips1W(String category) {

		logger.info("Fetching 1W tips | category={}", category);

		LocalDate today = LocalDate.now();
		LocalDate from = today.minusDays(7);

		return getTipsBetween(category, from, today);
	}

	// 🔹 MONTH (1M)
	@Override
	public List<DailyTipHistoryEntity> getTips1M(String category) {

		logger.info("Fetching 1M tips | category={}", category);

		LocalDate today = LocalDate.now();
		LocalDate from = today.minusDays(30);

		return getTipsBetween(category, from, today);
	}

	// 🔹 CUSTOM DATE RANGE
	@Override
	public List<DailyTipHistoryEntity> getTipsByDateRange(String category, LocalDate from, LocalDate to) {

		logger.info("Fetching tips by date range | category={}, from={}, to={}", category, from, to);

		LocalDateTime start = from.atStartOfDay();
		LocalDateTime end = to.atTime(23, 59, 59);

		if (category != null && !category.isBlank()) {
			return repo.findByCategoryAndCreatedAtBetween(category.toUpperCase(), start, end);
		}

		return repo.findByCreatedAtBetween(start, end);
	}
}
