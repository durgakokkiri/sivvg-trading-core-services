package com.sivvg.tradingservices.service;

import org.springframework.stereotype.Service;

@Service
public interface YahooFinanceClient {

	public double fetchCurrentPrice(String stockCode);
}
