package com.sivvg.tradingservices.service;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
//
//	void sendTargetReachedNotification(String stockCode, String string, Double price);
//	
//	  // 🔔 New method
//    void sendGeneralNotification(String type, String message);

	public void sendTargetNotification(String stockCode, String category, String targetType, Double price);

	public void sendGeneralNotification(String type, String message);
}
