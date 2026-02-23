package com.sivvg.tradingservices.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.model.DailyTipHistoryEntity;
import com.sivvg.tradingservices.repository.DailyTipHistoryRepository;
import com.sivvg.tradingservices.repository.DailyTipRepository;
import com.sivvg.tradingservices.service.MarketHolidayService;
import com.sivvg.tradingservices.service.NotificationService;
import com.sivvg.tradingservices.service.PriceTrackerScheduler;
import com.sivvg.tradingservices.service.YahooFinanceClient;

import jakarta.transaction.Transactional;

@Service
public class PriceTrackerSchedulerServiceImpl implements PriceTrackerScheduler {

	private static final Logger logger = LoggerFactory.getLogger(PriceTrackerSchedulerServiceImpl.class);

	@Autowired
	private YahooFinanceClient yahooFinanceClient;

	@Autowired
	private DailyTipRepository tipRepository;

	@Autowired
	private DailyTipHistoryRepository historyRepo;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private MarketHolidayService marketHolidayService;

	// 🔹 PRICE TRACKER (EVERY 30 SECONDS)
	@Scheduled(fixedRate = 30000)
	public void trackPrices() {

		ZoneId zone = ZoneId.of("Asia/Kolkata");
		//LocalTime now = LocalTime.now(zone);
		LocalTime now = getCurrentTime(); 
		// 🔒 Skip holidays
		if (marketHolidayService.isMarketHoliday()) {
			logger.debug("Market holiday – price tracking skipped");
			return;
		}

		// 🔒 Market hours check
		if (now.isBefore(LocalTime.of(9, 15)) || now.isAfter(LocalTime.of(15, 30))) {
			logger.debug("Outside market hours – price tracking skipped");
			return;
		}

		List<DailyTipEntity> tips = tipRepository.findAll();

		if (tips.isEmpty()) {
			logger.info("No daily tips found – sending NO_TIPS notification");
			notificationService.sendGeneralNotification("NO_TIPS", "Today no tips provided");
			return;
		}

		LocalDateTime nowTime = LocalDateTime.now(zone);

		for (DailyTipEntity tip : tips) {

			Double livePriceDouble = yahooFinanceClient.fetchCurrentPrice(tip.getStockCode());

			if (livePriceDouble == null || livePriceDouble <= 0) {
				logger.debug("Invalid live price skipped | stockCode={}", tip.getStockCode());
				continue;
			}

			Double old = tip.getCurrentPrice() != null ? tip.getCurrentPrice() : tip.getStockPrice();

			BigDecimal previousPrice = BigDecimal.valueOf(old);
			BigDecimal livePrice = BigDecimal.valueOf(livePriceDouble);
			BigDecimal stockPrice = BigDecimal.valueOf(tip.getStockPrice());

			boolean buy = "BUY".equalsIgnoreCase(tip.getStockType());
			boolean sell = "SELL".equalsIgnoreCase(tip.getStockType());

			boolean updated = false;

			String category = tip.getCategory() != null ? tip.getCategory().toUpperCase() : "";

			// 🔹 EQUITY / FUTURES TARGETS
			if (category.equals("EQUITY") || category.equals("FUTURES")) {

				if (tip.getT1() != null)
					updated |= checkEquityTarget(tip, "T1", livePrice, BigDecimal.valueOf(tip.getT1()), previousPrice,
							buy, sell, nowTime);

				if (tip.getT2() != null)
					updated |= checkEquityTarget(tip, "T2", livePrice, BigDecimal.valueOf(tip.getT2()), previousPrice,
							buy, sell, nowTime);

				if (tip.getT3() != null)
					updated |= checkEquityTarget(tip, "T3", livePrice, BigDecimal.valueOf(tip.getT3()), previousPrice,
							buy, sell, nowTime);
			}

			// 🔹 OPTIONS TARGET
			if (category.equals("OPTIONS")) {

				if (tip.getTargetPrice() != null && !Boolean.TRUE.equals(tip.getTargetReached())) {

					updated |= checkOptionTarget(tip, "TARGET", livePrice, BigDecimal.valueOf(tip.getTargetPrice()),
							previousPrice, buy, sell);
				}
			}

			// 🔹 PRICE UPDATE
			if (tip.getCurrentPrice() == null || BigDecimal.valueOf(tip.getCurrentPrice()).compareTo(livePrice) != 0) {

				tip.setPreviousPrice(old);
				tip.setCurrentPrice(livePrice.doubleValue());
				tip.setPriceDifference(livePrice.subtract(stockPrice).doubleValue());
				updated = true;
			}

			if (updated) {
				tipRepository.save(tip);
				logger.debug("Tip updated | stockCode={}", tip.getStockCode());
			}
		}
	}

	// 🔹 EQUITY / FUTURES TARGET CHECK
	private boolean checkEquityTarget(DailyTipEntity tip, String targetName, BigDecimal livePrice,
			BigDecimal targetPrice, BigDecimal previousPrice, boolean buy, boolean sell, LocalDateTime nowTime) {

		boolean crossed = (buy && previousPrice.compareTo(targetPrice) < 0 && livePrice.compareTo(targetPrice) >= 0)
				|| (sell && previousPrice.compareTo(targetPrice) > 0 && livePrice.compareTo(targetPrice) <= 0);

		if (!crossed)
			return false;

		switch (targetName) {
		case "T1":
			if (Boolean.TRUE.equals(tip.getT1Reached()))
				return false;
			tip.setT1Reached(true);
			tip.setT1ReachedTime(nowTime);
			break;
		case "T2":
			if (Boolean.TRUE.equals(tip.getT2Reached()))
				return false;
			tip.setT2Reached(true);
			tip.setT2ReachedTime(nowTime);
			break;
		case "T3":
			if (Boolean.TRUE.equals(tip.getT3Reached()))
				return false;
			tip.setT3Reached(true);
			tip.setT3ReachedTime(nowTime);
			break;
		}

		logger.info("Target reached | stockCode={}, target={}", tip.getStockCode(), targetName);

		notificationService.sendTargetNotification(tip.getStockCode(), tip.getCategory(), targetName,
				livePrice.doubleValue());

		return true;
	}

	// 🔹 OPTIONS TARGET CHECK
	private boolean checkOptionTarget(DailyTipEntity tip, String targetName, BigDecimal livePrice,
			BigDecimal targetPrice, BigDecimal previousPrice, boolean buy, boolean sell) {

		boolean crossed = (buy && previousPrice.compareTo(targetPrice) < 0 && livePrice.compareTo(targetPrice) >= 0)
				|| (sell && previousPrice.compareTo(targetPrice) > 0 && livePrice.compareTo(targetPrice) <= 0);

		if (!crossed)
			return false;

		tip.setTargetReached(true);

		logger.info("Options target reached | stockCode={}, target={}", tip.getStockCode(), targetName);

		notificationService.sendTargetNotification(tip.getStockCode(), "OPTIONS", targetName, livePrice.doubleValue());

		return true;
	}

	// 🔹 ARCHIVE AT 4 PM
	@Transactional
	@Scheduled(cron = "0 0 16 * * MON-FRI", zone = "Asia/Kolkata")
	public void archiveAt4PM() {

		ZoneId zone = ZoneId.of("Asia/Kolkata");
		LocalDate today = LocalDate.now(zone);

		LocalDateTime startOfDay = today.atStartOfDay();
		LocalDateTime endOfDay = today.atTime(23, 59, 59);

		List<DailyTipEntity> tips = tipRepository.findByCreatedAtBetween(startOfDay, endOfDay);

		if (tips.isEmpty()) {
			logger.info("No records to archive today");
			return;
		}

		logger.info("Archiving {} daily tips", tips.size());

		for (DailyTipEntity tip : tips) {

			DailyTipHistoryEntity h = new DailyTipHistoryEntity();

			h.setStockCode(tip.getStockCode());
			h.setStockPrice(tip.getStockPrice());
			h.setStockType(tip.getStockType());
			h.setCategory(tip.getCategory());
			h.setPlanType(tip.getPlanType());
			h.setSectorType(tip.getSectorType());

			h.setCurrentPrice(tip.getCurrentPrice());
			h.setPriceDifference(tip.getPriceDifference());

			h.setCreatedAt(tip.getCreatedAt());
			h.setPostedTime(tip.getPostedTime());
			h.setUpdatedAt(LocalDateTime.now());

			h.setT1(tip.getT1());
			h.setT2(tip.getT2());
			h.setT3(tip.getT3());

			h.setT1Reached(tip.getT1Reached());
			h.setT2Reached(tip.getT2Reached());
			h.setT3Reached(tip.getT3Reached());

			h.setT1ReachedTime(tip.getT1ReachedTime());
			h.setT2ReachedTime(tip.getT2ReachedTime());
			h.setT3ReachedTime(tip.getT3ReachedTime());

			h.setOptionType(tip.getOptionType());
			h.setStrikePrice(tip.getStrikePrice());
			h.setAtm(tip.getAtm());
			h.setOtm(tip.getOtm());
			h.setTargetPrice(tip.getTargetPrice());
			h.setTargetReached(tip.getTargetReached());

			historyRepo.save(h);
		}

		tipRepository.deleteAll(tips);

		logger.info("Archived & deleted {} daily tips at 4 PM", tips.size());
	}
	
	protected LocalTime getCurrentTime() {
	    return LocalTime.now(ZoneId.of("Asia/Kolkata"));
	}

}