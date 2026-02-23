package com.sivvg.tradingservices.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
@Service
public interface PortfolioRecordService {

	public List<PortfolioDailyRecord> getAllDailyRecords();

	public PortfolioDailyRecord getDailyRecordById(Long id);

	public PortfolioDailyRecord updateDailyRecord(Long id, PortfolioDailyRecord record);

	public PortfolioDailyRecord patchDailyRecord(Long id, Map<String, Object> updates);

	public void deleteDailyRecord(Long id);

	public List<PortfolioMonthlyUserSummary> getAllMonthlySummaries();

	public void deleteMonthlySummary(Long id);

	public Object getMonthlyByMonth(String month);
}
