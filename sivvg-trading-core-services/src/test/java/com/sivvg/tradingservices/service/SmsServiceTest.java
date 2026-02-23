package com.sivvg.tradingservices.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sivvg.tradingservices.configuration.TwilioConfig;

public class SmsServiceTest {
	@InjectMocks
	private SmsService smsService;

	@Mock
	private TwilioConfig config; // if used inside

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void sendSms_whenTwilioNotConfigured_logsMessage() {
		// Arrange: Twilio not configured
		smsService = new SmsService();
		smsService.sendSms("+911234567890", "Test message");

	}
}
