package com.sivvg.tradingservices.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.DailyTipHistoryEntity;
import com.sivvg.tradingservices.playload.CategoryPerformance;
@Service
public interface PerformanceService {



	public List<DailyTipHistoryEntity> getTips1D(String category);

	public List<DailyTipHistoryEntity> getTips1W(String category);

	public List<DailyTipHistoryEntity> getTips1M(String category);

	public List<DailyTipHistoryEntity> getTipsByDateRange(String category, LocalDate from, LocalDate to);

	public CategoryPerformance getCategoryPerformance(String category);

}
