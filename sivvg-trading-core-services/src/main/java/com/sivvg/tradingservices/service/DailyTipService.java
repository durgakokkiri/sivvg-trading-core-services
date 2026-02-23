package com.sivvg.tradingservices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.DailyTipRequest;
@Service
public interface DailyTipService {

	public DailyTipEntity saveTip(DailyTipRequest request);

	public List<DailyTipEntity> getAllTips();

	public DailyTipEntity getTipStatus(Long tipId);

	public void deleteTip(Long tipId);

	public DailyTipEntity updateTip(Long tipId, DailyTipRequest req);

	public List<DailyTipEntity> getTipsByCategory(String category);

}
