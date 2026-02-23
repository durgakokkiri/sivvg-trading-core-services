package com.sivvg.tradingservices.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.repository.MarketHolidayRepository;
import com.sivvg.tradingservices.service.MarketHolidayService;

@Service
public class MarketHolidayServiceImpl implements MarketHolidayService {

	private static final Logger logger = LoggerFactory.getLogger(MarketHolidayServiceImpl.class);

	@Autowired
	private MarketHolidayRepository repo;

	private static final ZoneId ZONE = ZoneId.of("Asia/Kolkata");

	// 🔹 CHECK TODAY MARKET HOLIDAY
	@Override
	public boolean isMarketHoliday() {

		LocalDate today = LocalDate.now(ZONE);

		logger.info("Checking market holiday for today | date={}", today);

		boolean isHoliday = isMarketHoliday(today);

		logger.info("Market holiday status for today: {}", isHoliday);
		return isHoliday;
	}

	// 🔹 CHECK MARKET HOLIDAY FOR GIVEN DATE
	@Override
	public boolean isMarketHoliday(LocalDate date) {

		logger.info("Checking market holiday | date={}", date);

		DayOfWeek day = date.getDayOfWeek();

		// Weekend check
		if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {

			logger.debug("Date falls on weekend | day={}", day);
			return true;
		}

		boolean exists = repo.existsByHolidayDate(date);

		logger.debug("Holiday exists in database for {} : {}", date, exists);
		return exists;
	}
}
