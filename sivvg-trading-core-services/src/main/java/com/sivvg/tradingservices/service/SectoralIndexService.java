package com.sivvg.tradingservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.SectorPerformance;

@Service
public interface SectoralIndexService {

	public List<SectorPerformance> getTodaySectorTypes();

	public List<SectorPerformance> getTodaySectorTypeCount();

	public List<DailyTipEntity> getTodayCompaniesBySector(String sectorType);

}
