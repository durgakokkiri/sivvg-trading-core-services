package com.sivvg.tradingservices.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.sivvg.tradingservices.model.DeviceTokenEntity;
import com.sivvg.tradingservices.repository.DeviceTokenRepository;
import com.sivvg.tradingservices.service.PushNotificationService;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

	@Autowired
	private DeviceTokenRepository tokenRepo;

	@Override
	public void sendPushToAll(String title, String body) {

		logger.info("Push notification broadcast started | title={}", title);

		List<DeviceTokenEntity> tokens = tokenRepo.findAll();

		if (tokens.isEmpty()) {
			logger.warn("No device tokens found for push notification");
			return;
		}

		logger.info("Total device tokens found: {}", tokens.size());

		for (DeviceTokenEntity t : tokens) {
			try {
				Message message = Message.builder().setToken(t.getToken()).putData("title", title).putData("body", body)
						.build();

				FirebaseMessaging.getInstance().sendAsync(message);

				logger.debug("Push notification sent | tokenId={}", t.getId());

			} catch (Exception e) {

				logger.error("Invalid FCM token detected, deleting token | tokenId={}", t.getId(), e);

				tokenRepo.delete(t);
			}
		}

		logger.info("Push notification broadcast completed");
	}
}
