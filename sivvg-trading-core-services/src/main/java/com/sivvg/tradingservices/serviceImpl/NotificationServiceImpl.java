package com.sivvg.tradingservices.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.NotificationEntity;
import com.sivvg.tradingservices.repository.NotificationRepository;
import com.sivvg.tradingservices.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	@Autowired
	private NotificationRepository repo;

	// 🔹 TARGET NOTIFICATION
	@Override
	public void sendTargetNotification(String stockCode, String category, String targetType, Double price) {

		logger.info("Send target notification started | stockCode={}, targetType={}", stockCode, targetType);

		String msg = "TARGET REACHED - " + stockCode + " " + targetType + " @ " + price;

		NotificationEntity n = new NotificationEntity();
		n.setStockCode(stockCode);
		n.setCategory(category);
		n.setTargetType(targetType);
		n.setPrice(price);
		n.setMessage(msg);

		repo.save(n); // 🔥 DB SAVE

		logger.info("Target notification saved successfully | stockCode={}, targetType={}", stockCode, targetType);

		// FUTURE:
		// Firebase / FCM push notifications
	}

	// 🔹 GENERAL NOTIFICATION
	@Override
	public void sendGeneralNotification(String type, String message) {

		logger.info("Send general notification started | type={}", type);

		NotificationEntity n = new NotificationEntity();
		n.setCategory(type);
		n.setMessage(message);

		repo.save(n);

		logger.info("General notification saved successfully | type={}", type);
	}
}
