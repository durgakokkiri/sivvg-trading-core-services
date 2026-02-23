package com.sivvg.tradingservices.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
@Service
public interface MarketHolidayService {

	public boolean isMarketHoliday();

	public boolean isMarketHoliday(LocalDate date);
}
