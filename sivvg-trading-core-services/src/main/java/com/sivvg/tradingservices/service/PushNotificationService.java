package com.sivvg.tradingservices.service;

import org.springframework.stereotype.Service;

@Service
public interface PushNotificationService {

	public void sendPushToAll(String title, String body);
}